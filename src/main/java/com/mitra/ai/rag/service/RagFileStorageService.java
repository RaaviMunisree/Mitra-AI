package com.mitra.ai.rag.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RagFileStorageService {

    private final Path storageLocation;

    public RagFileStorageService(Path ragStorageLocation) {
        this.storageLocation = ragStorageLocation;
    }

    public String store(MultipartFile file, String documentId)
            throws IOException {

        Files.createDirectories(storageLocation);

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(
                originalName.lastIndexOf('.'));

        String storedName = UUID.randomUUID() + extension;

        Path target = storageLocation.resolve(storedName).normalize();

        if (!target.startsWith(storageLocation)) {
            throw new IllegalArgumentException("Invalid storage path.");
        }

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, target,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        return target.toString();
    }

    public byte[] read(String storagePath) throws IOException {
        Path path = validateStoragePath(storagePath);
        return Files.readAllBytes(path);
    }

    public void delete(String storagePath) throws IOException {
        Path path = validateStoragePath(storagePath);
        Files.deleteIfExists(path);
    }

    private Path validateStoragePath(String storagePath) {

        Path path = Path.of(storagePath).toAbsolutePath().normalize();

        if (!path.startsWith(storageLocation)) {
            throw new IllegalArgumentException(
                    "File path is outside the configured storage directory.");
        }

        return path;
    }
}