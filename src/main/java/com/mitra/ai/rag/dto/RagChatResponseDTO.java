package com.mitra.ai.rag.dto;

import java.util.List;

public record RagChatResponseDTO(
        String conversationId,
        String question,
        String answer,
        int retrievedChunks,
        boolean contextFound,
        List<RagSourceDTO> sources
) {
}