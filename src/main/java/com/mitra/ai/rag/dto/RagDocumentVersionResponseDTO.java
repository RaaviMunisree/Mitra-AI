package com.mitra.ai.rag.dto;

import java.time.Instant;

public record RagDocumentVersionResponseDTO(
        Long id,
        String documentId,
        int versionNumber,
        String fileName,
        String checksum,
        int chunkCount,
        Instant createdAt
) {
}