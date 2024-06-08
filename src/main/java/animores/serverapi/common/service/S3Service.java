package animores.serverapi.common.service;

import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    List<PutObjectRequest> uploadFilesToS3(List<MultipartFile> files, String path)
        throws IOException;

    PutObjectRequest uploadFileToS3(MultipartFile file, String path) throws IOException;

    void removeFilesFromS3(List<String> urls);

    CompletableFuture<Void> uploadFilesToS3_temp(List<MultipartFile> file, List<String> urls);
}
