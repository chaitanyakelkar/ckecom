package org.ecommerce.ckecom.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadImage(String path, MultipartFile file) throws IOException;
    void deleteImage(String path, String imageFileName) throws IOException;
}
