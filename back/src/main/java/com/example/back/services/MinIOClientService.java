package com.example.back.services;

import java.io.InputStream;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MinIOClientService {
    private static final String bucketName = "imports";

    MinioClient minioClient;

    @PostConstruct
    void setUp() {
        minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minio", "minio123")
                .build();
    }

    public void uploadFile(InputStream fileIS, String objectName) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(fileIS, -1, 10485760)
                    .contentType("application/octet-stream")
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    InputStream getFile(String objectName) {
        try {
            InputStream is = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}