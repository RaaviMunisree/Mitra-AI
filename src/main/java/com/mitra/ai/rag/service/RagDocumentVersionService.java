package com.mitra.ai.rag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mitra.ai.rag.dto.RagDocumentVersionResponseDTO;
import com.mitra.ai.rag.repository.RagDocumentRepository;
import com.mitra.ai.rag.repository.RagDocumentVersionRepository;

@Service
public class RagDocumentVersionService {

    private final RagDocumentRepository documentRepository;
    private final RagDocumentVersionRepository versionRepository;

    public RagDocumentVersionService(
            RagDocumentRepository documentRepository,
            RagDocumentVersionRepository versionRepository) {

        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    public List<RagDocumentVersionResponseDTO> getVersionHistory(
            String documentId) {

        if (!documentRepository.existsById(documentId)) {
            throw new IllegalArgumentException(
                    "Document not found: " + documentId);
        }

        return versionRepository
                .findByDocumentIdOrderByVersionNumberDesc(documentId)
                .stream()
                .map(version -> new RagDocumentVersionResponseDTO(
                        version.getId(),
                        version.getDocumentId(),
                        version.getVersionNumber(),
                        version.getFileName(),
                        version.getChecksum(),
                        version.getChunkCount(),
                        version.getCreatedAt()
                ))
                .toList();
    }
}