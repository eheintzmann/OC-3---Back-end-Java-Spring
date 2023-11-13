package com.openclassrooms.api.service.storage;

import com.openclassrooms.api.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(@Value("${app.storage.path}") String appStorage) {

        if (appStorage.trim().isEmpty()) {
            log.error("File upload location can not be empty.");
            throw new StorageException("File upload location can not be empty.");
        }
        this.rootLocation = Path.of(appStorage);
    }


    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException | UnsupportedOperationException | SecurityException ex) {
            log.error("Could not init " + rootLocation + " upload directory");
            throw new StorageException("Could not init " + rootLocation + " upload directory", ex);
        }
    }


    @Override
    public String store(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file !");
        }
        if (file.getOriginalFilename() == null) {
            throw new StorageException("Original filename is null !");
        }

        try {
            final Path destinationDir = this.rootLocation.resolve(Path.of(subDir)).normalize().toAbsolutePath();
            final Path tmpDestinationFile = destinationDir.resolve(Path.of(file.getOriginalFilename())).normalize().toAbsolutePath();
            final Path destinationFile = getUniqueFileName(tmpDestinationFile);

            if (!destinationFile.startsWith(this.rootLocation.toAbsolutePath())) {
                // Security check
                throw new StorageException("Cannot store file outside root location.");
            }

            Files.createDirectories(destinationDir);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return destinationFile.getFileName().toString();

        } catch (IOException | UnsupportedOperationException | SecurityException ex) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename() + " : " + ex.getMessage(), ex);
        }
    }

    private Path getUniqueFileName(Path originalPath) {

        final Path parentDir = originalPath.getParent();
        final String fileName = originalPath.getFileName().toString();

        int num = 0;
        final String ext = StringUtils.getFilenameExtension(fileName);
        final String name = StringUtils.stripFilenameExtension(fileName);

        Path destinationPath = parentDir.resolve(Path.of(name + "." + ext));
        while (Files.exists(destinationPath)) {
            num++;
            destinationPath = parentDir.resolve(Path.of(name + "-" + num + "." + ext));
        }
        return destinationPath;
    }
}
