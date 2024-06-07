package animores.serverapi.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    void uploadFilesToS3(List<MultipartFile> files, String path, List<String> fileNames)
        throws IOException;

    void uploadFileToS3(MultipartFile file, String path, String fileName) throws IOException;

    void removeFilesFromS3(List<String> urls);

}
