package com.openclassrooms.api.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Store files
 */
public interface StorageService {

    void init();

    String store(MultipartFile file, String subDir);

}
