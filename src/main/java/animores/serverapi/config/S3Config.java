package animores.serverapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
        return S3Client.builder()
            .credentialsProvider(awsCredentialsProvider)
            .region(Region.of(region))
            .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient(AwsCredentialsProvider awsCredentialsProvider) {
        return S3AsyncClient.builder()
            .region(Region.of(region))
            .credentialsProvider(awsCredentialsProvider)
            .httpClientBuilder(NettyNioAsyncHttpClient.builder())
            .serviceConfiguration(S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .build())
            .build();
    }
}
