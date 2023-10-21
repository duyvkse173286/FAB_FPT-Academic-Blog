package com.fpt.blog.services;

import com.fpt.blog.utils.UploadMedia;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * Upload file to storage
     * @param file
     * @return path to file
     */
    UploadMedia uploadFile(MultipartFile file);
}
