package animores.serverapi.profile.service.impl;

import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.service.ProfileBatchService;
import jakarta.persistence.EntityManagerFactory;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProfileBatchServiceImpl implements ProfileBatchService {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final AccountRepository accountRepository;

    @Override
    public void insertProfileBatch(Integer count, Integer accountStartId) {
        try{
            jobLauncher.run(
                    new JobBuilder("profileBatchInsertJob", jobRepository)
                            .incrementer(new RunIdIncrementer())
                            .start(profileBatchInsertStep(count,accountStartId))
                            .build()
                    , new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    protected Step profileBatchInsertStep(Integer count, Integer accountStartId) {
        return new StepBuilder("profileBatchInsertStep", jobRepository)
                .<Profile, Profile>chunk(100, transactionManager)
                .reader(new ProfileBatchServiceImpl.ProfileBatchInsertFactory(count, accountStartId, accountRepository))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    private static class ProfileBatchInsertFactory implements ItemReader<Profile> {
        private int currentIdx = 0;
        private final int accountStartId;
        private final int count;
        private final AccountRepository accountRepository;

        public ProfileBatchInsertFactory(Integer count, Integer accountStartId, AccountRepository accountRepository) {
            this.count = count;
            this.accountRepository = accountRepository;
            this.accountStartId = accountStartId;
        }

        @Override
        public Profile read() throws Exception {
            if (currentIdx < count) {
                currentIdx++;
                String randomString = UUID.randomUUID().toString();
                return Profile.builder()
                        .account(this.accountRepository.getReferenceById((long)(currentIdx/3 + accountStartId)))
                        .name(randomString.substring(0,10) + currentIdx)
                        .imageUrl(randomString.substring(10))
                        .build();
            } else {
                return null;
            }
        }
    }

    private ItemProcessor<Profile, Profile> itemProcessor() {
        return item -> item;
    }

    private JpaItemWriter<Profile> itemWriter() {
        JpaItemWriter<Profile> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

}
