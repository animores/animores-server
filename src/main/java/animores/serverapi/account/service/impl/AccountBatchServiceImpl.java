package animores.serverapi.account.service.impl;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.service.AccountBatchService;
import animores.serverapi.account.type.Role;
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

@Service
@RequiredArgsConstructor
public class AccountBatchServiceImpl implements AccountBatchService {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void insertAccountBatch(Integer count) {
        try{
            jobLauncher.run(
                    new JobBuilder("accountBatchInsertJob", jobRepository)
                            .incrementer(new RunIdIncrementer())
                            .start(accountBatchInsertStep(count))
                            .build()
                    , new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    protected Step accountBatchInsertStep(Integer count) {
        return new StepBuilder("accountBatchInsertStep", jobRepository)
                .<Account, Account>chunk(100, transactionManager)
                .reader(new AccountBatchInsertFactory(count))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }
    private static class AccountBatchInsertFactory implements ItemReader<Account> {
        private int currentIdx = 0;
        private final int count;
        public AccountBatchInsertFactory(Integer count) {
            this.count = count;
        }

        @Override
        public Account read() throws Exception {
            if (currentIdx < count) {
                String randomString = UUID.randomUUID().toString();
                return Account.builder()
                        .role(Role.USER)
                        .email(randomString.substring(0,15) + currentIdx + "@test.com")
                        .password(randomString.substring(0, 10))
                        .nickname(randomString.substring(10, 20)+currentIdx)
                        .isAdPermission(false)
                        .build();
            } else {
                return null;
            }
        }
    }

    public ItemProcessor<Account, Account> itemProcessor() {
        return item -> item;
    }

    public JpaItemWriter<Account> itemWriter() {
        JpaItemWriter<Account> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

}
