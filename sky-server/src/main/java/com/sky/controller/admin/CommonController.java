package com.sky.controller.admin;/**
 * @description
 * @author starlord
 * @create 2025/5/27
 */

import cn.hutool.crypto.digest.DigestUtil;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.FileUtils;
import com.sky.utils.MinioUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author starlord
 * @description
 * @create 2025/5/27
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private RedisTemplate<String,Object>  redisTemplate;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        try {
            String fileMd5 = DigestUtil.md5Hex(file.getInputStream());
            Object cacheResult = redisTemplate.opsForValue().get(fileMd5);
            if(cacheResult!=null){
                return Result.isOk("该文件已经上传过",cacheResult.toString());
            }
            String objUrl = minioUtils.uploadFile(file, file.getOriginalFilename());
            redisTemplate.opsForValue().set(fileMd5, objUrl);
            return Result.success(objUrl);
        } catch (Exception e) {
            log.error("文件上传失败：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
