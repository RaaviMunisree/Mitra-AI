package com.mitra.ai.rag.controller;

import java.util.Map;

import com.mitra.ai.rag.service.RagReindexService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag/documents")
public class RagReindexController {

    private final RagReindexService reindexService;

    public RagReindexController(RagReindexService reindexService) {
        this.reindexService = reindexService;
    }

    @PostMapping("/{documentId}/reindex")
    public ResponseEntity<Map<String, Object>> reindexDocument(
            @PathVariable String documentId) {

        int chunks = reindexService.reindexDocument(documentId);

        return ResponseEntity.ok(Map.of(
                "documentId", documentId,
                "status", "COMPLETED",
                "generatedChunks", chunks,
                "message", "Document successfully re-indexed."
        ));
    }
}