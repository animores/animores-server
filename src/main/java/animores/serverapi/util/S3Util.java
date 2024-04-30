package animores.serverapi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public List<PutObjectRequest> uploadFilesToS3(List<MultipartFile> files, String path)
        throws IOException {
        List<PutObjectRequest> putObjectRequests = new ArrayList<>();

        for (MultipartFile file : files) {
            putObjectRequests.add(uploadFileToS3(file, path));
        }

        return putObjectRequests;
    }

    public PutObjectRequest uploadFileToS3(MultipartFile file, String path) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .contentType(file.getContentType())
            .contentLength(file.getSize())
            .key(path + UUID.randomUUID())
            .build();
        RequestBody requestBody = RequestBody.fromBytes(file.getBytes());
        s3Client.putObject(putObjectRequest, requestBody);

        return putObjectRequest;
    }

    public void removeFilesFromS3(List<ObjectIdentifier> keys) {
        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(b -> b.objects(keys))
            .build();

        s3Client.deleteObjects(deleteObjectsRequest);
    }
}
