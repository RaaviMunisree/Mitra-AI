package com.mitra.ai.rag.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mitra.ai.rag.dto.RagDocumentResponseDTO;
import com.mitra.ai.rag.entity.RagDocument;
import com.mitra.ai.rag.entity.RagDocumentVersion;
import com.mitra.ai.rag.repository.RagDocumentRepository;
import com.mitra.ai.rag.repository.RagDocumentVersionRepository;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class RagDocumentManagementService {

    private final RagDocumentRepository documentRepository;
    private final RagDocumentVersionRepository versionRepository;
    private final VectorStore vectorStore;
    private final RagFileStorageService fileStorageService;

    public RagDocumentManagementService(
            RagDocumentRepository documentRepository,
            RagDocumentVersionRepository versionRepository,
            VectorStore vectorStore,
            RagFileStorageService fileStorageService) {

        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.vectorStore = vectorStore;
        this.fileStorageService = fileStorageService;
    }

    public List<RagDocumentResponseDTO> getAllDocuments() {

        return documentRepository.findAllByOrderByUploadedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RagDocumentResponseDTO getDocumentById(String documentId) {

        RagDocument document = findDocument(documentId);

        return mapToResponse(document);
    }

    public void deleteDocument(String documentId) {

        RagDocument document = findDocument(documentId);

        List<RagDocumentVersion> versions =
                versionRepository
                        .findByDocumentIdOrderByVersionNumberDesc(documentId);

        Set<String> storagePaths = new LinkedHashSet<>();

        if (document.getStoragePath() != null) {
            storagePaths.add(document.getStoragePath());
        }

        for (RagDocumentVersion version : versions) {
            if (version.getStoragePath() != null) {
                storagePaths.add(version.getStoragePath());
            }
        }

        try {
            document.setStatus("DELETING");
            documentRepository.save(document);

            FilterExpressionBuilder filterBuilder =
                    new FilterExpressionBuilder();

            // Remove all indexed chunks belonging to this document.
            vectorStore.delete(
                    filterBuilder.eq("documentId", documentId).build());

            // Remove current and historical original files.
            for (String storagePath : storagePaths) {
                fileStorageService.delete(storagePath);
            }

            // Remove version history and document metadata.
            versionRepository.deleteAllByDocumentId(documentId);
            documentRepository.delete(document);

        } catch (Exception exception) {

            document.setStatus("DELETION_FAILED");
            documentRepository.save(document);

            throw new IllegalStateException(
                    "Document deletion was incomplete. "
                    + "The document is marked for deletion recovery.",
                    exception);
        }
    }

    private RagDocument findDocument(String documentId) {

        if (documentId == null || documentId.isBlank()) {
            throw new IllegalArgumentException(
                    "Document ID cannot be empty.");
        }

        return documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Document not found: " + documentId));
    }

    private RagDocumentResponseDTO mapToResponse(
            RagDocument document) {

        return new RagDocumentResponseDTO(
                document.getDocumentId(),
                document.getFileName(),
                document.getFileType(),
                document.getChunkCount(),
                document.getStatus(),
                document.getUploadedAt()
        );
    }
}