package com.mitra.ai.rag.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(
    name = "rag_document_versions",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_rag_document_version",
                columnNames = {"document_id", "version_number"})
    }
)
public class RagDocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", nullable = false, length = 36)
    private String documentId;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "storage_path", nullable = false, length = 1000)
    private String storagePath;

    @Column(name = "checksum", nullable = false, length = 64)
    private String checksum;

    @Column(name = "chunk_count", nullable = false)
    private int chunkCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public RagDocumentVersion() {
    }

    public RagDocumentVersion(
            String documentId,
            int versionNumber,
            String fileName,
            String storagePath,
            String checksum,
            int chunkCount,
            Instant createdAt) {

        this.documentId = documentId;
        this.versionNumber = versionNumber;
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.checksum = checksum;
        this.chunkCount = chunkCount;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getDocumentId() { return documentId; }
    public int getVersionNumber() { return versionNumber; }
    public String getFileName() { return fileName; }
    public String getStoragePath() { return storagePath; }
    public String getChecksum() { return checksum; }
    public int getChunkCount() { return chunkCount; }
    public Instant getCreatedAt() { return createdAt; }
}