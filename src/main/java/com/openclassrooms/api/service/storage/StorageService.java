package com.openclassrooms.api.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    Resource store(MultipartFile file, String subDir);

}
