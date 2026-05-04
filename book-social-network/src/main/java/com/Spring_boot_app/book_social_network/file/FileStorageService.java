package com.Spring_boot_app.book_social_network.file;
import com.Spring_boot_app.book_social_network.book.Book;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
@Service
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private final String fileUploadPath;

    public String saveFile(@NonNull MultipartFile sourceFile,
                           @NonNull int userId) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile,fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile,@NonNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath+ separator + fileUploadSubPath;
        File targetFolder = new File(fileUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Folder failed to be created");
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        // ./uploads/users/1/215479653217.jpg
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Filed saved to "+targetFilePath);
            return targetFilePath;
        } catch (IOException exp) {
            log.warn("File not saved");
        }
        return null;
    }

    private String getFileExtension(@Nullable String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return originalFilename.substring(lastDotIndex+1).toLowerCase();
    }
}
