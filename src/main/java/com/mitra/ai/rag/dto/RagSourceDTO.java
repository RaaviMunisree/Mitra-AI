package com.mitra.ai.rag.dto;

public record RagSourceDTO(
        String documentId,
        String fileName,
        String fileType,
        String chunkId,
        String content
) {
}