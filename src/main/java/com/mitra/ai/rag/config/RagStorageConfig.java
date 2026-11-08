package com.mitra.ai.rag.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagStorageConfig {

    @Bean
    public Path ragStorageLocation(
            @Value("${mitra.rag.storage.location}") String location) {

        return Paths.get(location).toAbsolutePath().normalize();
    }
}