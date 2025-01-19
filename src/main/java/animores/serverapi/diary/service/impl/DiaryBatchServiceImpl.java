package animores.serverapi.diary.service.impl;

import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryComment;
import animores.serverapi.diary.entity.DiaryLike;
import animores.serverapi.diary.entity.DiaryMedia;
import animores.serverapi.diary.entity.DiaryMediaType;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.service.DiaryBatchService;
import animores.serverapi.profile.entity.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Service
public class DiaryBatchServiceImpl implements DiaryBatchService {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private static final Random random = new Random();

    @Override
    public void insertDiaryBatch(Integer count, Long accountId) {
        try {
            jobLauncher.run(
                new JobBuilder("diaryBatchInsertJob", jobRepository)
                    .incrementer(new RunIdIncrementer())
                    .start(diaryBatchInsertStep(count, accountId))
                    .build()
                , new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private Step diaryBatchInsertStep(Integer count, Long accountId) {
        return new StepBuilder("diaryBatchInsertStep", jobRepository)
            .<Diary, Diary>chunk(100, transactionManager)
            .reader(
                new DiaryBatchInsertFactory(accountRepository, profileRepository, accountId, count))
            .processor(diaryItemProcessor())
            .writer(diaryItemWriter())
            .build();
    }

    private static class DiaryBatchInsertFactory implements ItemReader<Diary> {

        private int currentIdx = 0;
        private final AccountRepository accountRepository;
        private final List<Profile> profiles;
        private final long accountId;
        private final int count;

        public DiaryBatchInsertFactory(AccountRepository accountRepository,
            ProfileRepository profileRepository, Long accountId, int count) {
            this.accountRepository = accountRepository;
            this.profiles = profileRepository.findAllByAccountIdAndDeletedAtIsNull(accountId);
            this.accountId = accountId;
            this.count = count;
        }

        @Override
        public Diary read() throws Exception {
            if (currentIdx < count) {
                currentIdx++;
                String randomString = UUID.randomUUID().toString();
                return Diary.builder()
                    .content(randomString)
                    .account(accountRepository.getReferenceById(accountId))
                    .profile(profiles.get(currentIdx % profiles.size()))
                    .build();
            } else {
                return null;
            }
        }
    }

    private ItemProcessor<Diary, Diary> diaryItemProcessor() {
        return item -> item;
    }

    private JpaItemWriter<Diary> diaryItemWriter() {
        JpaItemWriter<Diary> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    @Override
    public void insertDiaryCommentBatch(Integer count, Long diaryId) {
        try {
            jobLauncher.run(
                new JobBuilder("diaryCommentBatchInsertJob", jobRepository)
                    .incrementer(new RunIdIncrementer())
                    .start(diaryCommentBatchInsertStep(count, diaryId))
                    .build()
                , new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private Step diaryCommentBatchInsertStep(Integer count, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(
            () -> new IllegalArgumentException("Diary not found")
        );

        List<Profile> profiles = profileRepository.findAllByAccountIdAndDeletedAtIsNull(
            diary.getAccount().getId());
        return new StepBuilder("diaryCommentBatchInsertStep", jobRepository)
            .<DiaryComment, DiaryComment>chunk(100, transactionManager)
            .reader(new DiaryCommentBatchInsertFactory(diary, profiles, count))
            .processor(diaryCommentItemProcessor())
            .writer(diaryCommentItemWriter())
            .build();
    }

    private static class DiaryCommentBatchInsertFactory implements ItemReader<DiaryComment> {

        private int currentIdx = 0;
        private final Diary diary;
        private final List<Profile> profiles;
        private final int count;

        public DiaryCommentBatchInsertFactory(Diary diary,
            List<Profile> profiles,
            int count) {
            this.diary = diary;
            this.profiles = profiles;
            this.count = count;
        }

        @Override
        public DiaryComment read() throws Exception {
            if (currentIdx < count) {
                currentIdx++;
                String randomString = UUID.randomUUID().toString();
                return DiaryComment.builder()
                    .diary(diary)
                    .profile(profiles.get(currentIdx % profiles.size()))
                    .content(randomString)
                    .build();
            } else {
                return null;
            }
        }
    }

    private ItemProcessor<DiaryComment, DiaryComment> diaryCommentItemProcessor() {
        return item -> item;
    }

    private JpaItemWriter<DiaryComment> diaryCommentItemWriter() {
        JpaItemWriter<DiaryComment> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    // TODO: 다이어리 좋아요의 경우 중복 처리를 어떻게 해줄 지에 대한 고민이 필요함
    @Override
    public void insertDiaryLikeBatch(Integer count, Long accountId) {
        try {
            jobLauncher.run(
                new JobBuilder("diaryLikeBatchInsertJob", jobRepository)
                    .incrementer(new RunIdIncrementer())
                    .start(diaryLikeBatchInsertStep(count, accountId))
                    .build()
                , new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private Step diaryLikeBatchInsertStep(Integer count, Long accountId) {
        return new StepBuilder("diaryLikeBatchInsertStep", jobRepository)
            .<Diary, DiaryLike>chunk(100, transactionManager)
            .reader(new JpaPagingItemReaderBuilder<Diary>()
                .name("diaryLikeBatchInsertReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                    "SELECT d FROM Diary d WHERE d.account.id = :accountId order by d.createdAt desc")
                .parameterValues(Map.of("accountId", accountId))
                .pageSize(100)
                .maxItemCount(count)
                .build())
            .processor(diaryLikeItemProcessor(
                profileRepository.findAllByAccountIdAndDeletedAtIsNull(accountId)))
            .writer(diaryLikeItemWriter())
            .build();
    }

    private ItemProcessor<Diary, DiaryLike> diaryLikeItemProcessor(List<Profile> profiles) {
        return item -> {
            Profile profile = profiles.get((int) (System.currentTimeMillis() % profiles.size()));
            return DiaryLike.builder()
                .diary(item)
                .profile(profile)
                .build();
        };
    }

    private JpaItemWriter<DiaryLike> diaryLikeItemWriter() {
        JpaItemWriter<DiaryLike> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    @Override
    public void insertDiaryMediaBatch(Integer count, Long accountId, Long maxDiaryId) {
        try {
            jobLauncher.run(
                new JobBuilder("diaryMediaBatchInsertJob", jobRepository)
                    .incrementer(new RunIdIncrementer())
                    .start(diaryMediaBatchInsertStep(count, accountId, maxDiaryId))
                    .build()
                , new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private Step diaryMediaBatchInsertStep(Integer count, Long accountId, Long maxDiaryId) {
        return new StepBuilder("diaryMediaBatchInsertStep", jobRepository)
            .<Diary, Diary>chunk(100, transactionManager)
            .reader(new JpaPagingItemReaderBuilder<Diary>()
                .name("diaryMediaBatchInsertReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                    "SELECT d FROM Diary d WHERE d.account.id = :accountId and d.id <= :maxDiaryId order by d.id desc")
                .parameterValues(Map.of("accountId", accountId, "maxDiaryId", maxDiaryId))
                .pageSize(100)
                .maxItemCount(count / 4)
                .build())
            .processor(diaryMediaItemProcessor())
            .writer(diaryMediaItemWriter())
            .build();
    }

    private ItemProcessor<Diary, Diary> diaryMediaItemProcessor() {
        return item -> {
            for (int i = 0; i < 4; i++) {
                item.getMedia().add(
                    DiaryMedia.create(
                        item,
                        UUID.randomUUID().toString(),
                        i,
                        DiaryMediaType.values()[random.nextInt(DiaryMediaType.values().length)]
                    )
                );
            }
            return item;
        };
    }

    private JpaItemWriter<Diary> diaryMediaItemWriter() {
        JpaItemWriter<Diary> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }
}
