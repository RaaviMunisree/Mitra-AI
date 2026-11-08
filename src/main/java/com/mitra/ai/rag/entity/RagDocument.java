package com.mitra.ai.rag.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(
    name = "rag_documents",
    indexes = {
        @Index(name = "idx_rag_document_uploaded_at",
                columnList = "uploaded_at"),
        @Index(name = "idx_rag_document_checksum",
                columnList = "checksum")
    }
)
public class RagDocument {

    @Id
    @Column(name = "document_id", nullable = false, length = 36)
    private String documentId;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_type", nullable = false, length = 20)
    private String fileType;

    @Column(name = "chunk_count", nullable = false)
    private int chunkCount;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "storage_path", length = 1000)
    private String storagePath;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Column(name = "version", nullable = false)
    private int version = 1;

    public RagDocument() {
    }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public int getChunkCount() { return chunkCount; }
    public void setChunkCount(int chunkCount) { this.chunkCount = chunkCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
}