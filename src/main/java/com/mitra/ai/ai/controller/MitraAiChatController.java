
package com.mitra.ai.ai.controller;

import java.util.Map;
import java.util.UUID;

import com.mitra.ai.ai.dto.AiChatResponseDTO;
import com.mitra.ai.ai.dto.ChatRequestDTO;
import com.mitra.ai.ai.service.MitraAiChatService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class MitraAiChatController {

    private final MitraAiChatService chatService;

    public MitraAiChatController(MitraAiChatService chatService) {
        this.chatService = chatService;
    }

    // General AI chat
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(
            @Valid @RequestBody ChatRequestDTO request) {

        String conversationId = resolveConversationId(request);

        AiChatResponseDTO response = chatService.generateResponse(
                conversationId,
                request.getPrompt()
        );

        return ResponseEntity.ok(
                Map.of(
                        "conversationId", conversationId,
                        "response", response
                )
        );
    }

    // Trip-specific AI chat
    @PostMapping("/trips/{tripId}/chat")
    public ResponseEntity<Map<String, Object>> chatWithTripContext(
            @PathVariable Long tripId,
            @Valid @RequestBody ChatRequestDTO request) {

        String conversationId = resolveConversationId(request);

        AiChatResponseDTO response = chatService.generateTripResponse(
                tripId,
                conversationId,
                request.getPrompt()
        );

        return ResponseEntity.ok(
                Map.of(
                        "conversationId", conversationId,
                        "response", response
                )
        );
    }

    // Generate a new conversation ID when one is not supplied.
    private String resolveConversationId(ChatRequestDTO request) {

        if (request.getConversationId() == null
                || request.getConversationId().isBlank()) {

            return UUID.randomUUID().toString();
        }

        return request.getConversationId();
    }
}
