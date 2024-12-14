package com.example.homework.controller;

import com.example.homework.config.FileStorageConfig;
import com.example.homework.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
public class FileUploadController {

    @Autowired
    private FileStorageConfig fileStorageConfig;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        // 获取配置的存储路径
        String storageLocation = fileStorageConfig.getStorageLocation();

        // 把文件的内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        // 保证文件的名字是唯一的, 从而防止文件覆盖
        String filename = originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File(storageLocation + filename));

        return Result.success(storageLocation);
    }
}