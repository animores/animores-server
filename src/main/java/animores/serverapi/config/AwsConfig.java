package animores.serverapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secretKey}")
    private String accessSecret;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, accessSecret));
    }

}
