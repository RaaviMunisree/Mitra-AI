package com.mitra.ai.rag.controller;

import com.mitra.ai.rag.dto.RagChatRequestDTO;
import com.mitra.ai.rag.dto.RagChatResponseDTO;
import com.mitra.ai.rag.service.RagGenerationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagChatController {

    private final RagGenerationService generationService;

    public RagChatController(
            RagGenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping("/chat")
    public ResponseEntity<RagChatResponseDTO> chatWithKnowledgeBase(
            @Valid @RequestBody RagChatRequestDTO request) {

        return ResponseEntity.ok(
                generationService.generateAnswer(request));
    }
}