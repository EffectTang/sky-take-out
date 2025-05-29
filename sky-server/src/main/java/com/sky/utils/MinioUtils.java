package com.sky.utils;/**
 * @description
 * @author starlord
 * @create 2025/5/26
 */

import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author starlord
 * @description
 * @create 2025/5/26
 */
@Component
public class MinioUtils {

    @Autowired
    private MinioClient  minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 生成新的文件名：UUID + 原始文件名
     */
    private String generateNewObjectName(String originalFilename) {
        String uniqueId = UUID.randomUUID().toString();
        int dotIndex = originalFilename.lastIndexOf('.');
        String prefix = (dotIndex != -1) ? originalFilename.substring(0, dotIndex) : originalFilename;
        String suffix = (dotIndex != -1) ? originalFilename.substring(dotIndex) : "";

        return uniqueId + "-" + prefix + suffix;
    }
    /**
     * 上传文件
     * @param file
     * @param objectName
     * @throws Exception
     */
    public String uploadFile(MultipartFile file, String objectName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // 2. 生成新的文件名
        String newObjectName = generateNewObjectName(file.getOriginalFilename());

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newObjectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        // 生成文件的公开访问 URL
        //TODO 如果 上传同一个图片时，怎么保证 不会重复上传，
        // 因为对图片的名称做了处理，所以MinIO server不会进行判重，即使同一张图片 被上传多次，仍会进行多次保存
        // 注意：确保 MinIO 配置允许匿名读取该对象或使用预签名 URL


        return new StringBuilder(endpoint).append("/").append(bucketName).append("/").append(newObjectName).toString();
    }

    /**
     * 下载文件
     * @param objectName
     * @return
     * @throws Exception
     */
    public GetObjectResponse downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    /**
     * 删除文件
     * @param objectName 文件名
     * @throws Exception
     */
    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

}
