package com.openclassrooms.api.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;

    public FileSystemStorageService(@Value("${app.storage.path}") String appStorage) {

        if(appStorage.trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }
        this.rootLocation = Path.of(appStorage);
    }

    @Override
    public Resource store(MultipartFile file, String  subDir) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationDir = this.rootLocation.resolve(Path.of(subDir)).normalize().toAbsolutePath();
            Path destinationFile = destinationDir.resolve(Path.of(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            mkdir(destinationDir);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

             Resource resource = new UrlResource(destinationFile.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException("Could not read file: " + file);
            }

        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + file, e);

        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }

    }

    private void mkdir(Path dir) {
        try {
            Files.createDirectories(dir);
        }
        catch (IOException e) {
            throw new StorageException("Could not create " + dir + " directory", e);
        }
    }

}
