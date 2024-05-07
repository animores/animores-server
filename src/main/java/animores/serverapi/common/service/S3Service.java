package animores.serverapi.common.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public interface S3Service {

    List<PutObjectRequest> uploadFilesToS3(List<MultipartFile> files, String path)
        throws IOException;

    PutObjectRequest uploadFileToS3(MultipartFile file, String path) throws IOException;

    void removeFilesFromS3(List<ObjectIdentifier> keys);

}
