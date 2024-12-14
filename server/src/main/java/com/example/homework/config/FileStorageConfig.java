package com.example.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Value("${file.storage.location}")
    private String storageLocation;

    public String getStorageLocation() {
        return storageLocation;
    }
}