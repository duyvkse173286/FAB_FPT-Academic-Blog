package com.fpt.blog.services.impl;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fpt.blog.configurations.AwsConfig;
import com.fpt.blog.services.FileService;
import com.fpt.blog.utils.UploadMedia;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final AwsConfig awsConfig;

    @Override
    @SneakyThrows
    public UploadMedia uploadFile(MultipartFile file) {
        File f = null;
        try {
            f = convertMultipartFileToFile(file);
            String fileName = generateUniqueFileName(file);
            uploadFile(fileName, f);

            return new UploadMedia()
                    .setUrl(getObjectUrl(fileName))
                    .setContentType(file.getContentType())
                    .setFileName(fileName);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
        return null;
    }

    @SneakyThrows
    private void uploadFile(String fileName, File file) {
        awsConfig.getS3Client().putObject(new PutObjectRequest(awsConfig.bucketName, fileName, file));
    }

    @SneakyThrows
    public String getObjectUrl(String fileName) {
        return "https://" + awsConfig.bucketName + ".s3." + awsConfig.region + ".amazonaws.com/" + fileName;
    }

    @SneakyThrows
    private static File convertMultipartFileToFile(MultipartFile multipartFile) {
        if (multipartFile == null || StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            return  null;
        }
        File cnvFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(cnvFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return cnvFile;
    }

    @SneakyThrows
    private static String generateUniqueFileName(MultipartFile multipartFile) {
        try {
            return new Date().getTime() + multipartFile.getOriginalFilename().replace(" ", "_");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Date().getTime() + "";
    }
}
