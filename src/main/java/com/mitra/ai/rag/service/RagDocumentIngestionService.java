package com.mitra.ai.rag.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mitra.ai.rag.dto.DocumentIngestionResponseDTO;
import com.mitra.ai.rag.entity.RagDocument;
import com.mitra.ai.rag.entity.RagDocumentVersion;
import com.mitra.ai.rag.repository.RagDocumentRepository;
import com.mitra.ai.rag.repository.RagDocumentVersionRepository;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RagDocumentIngestionService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("pdf", "docx", "txt");

    private final VectorStore vectorStore;
    private final RagDocumentRepository documentRepository;
    private final RagDocumentVersionRepository versionRepository;
    private final RagFileStorageService fileStorageService;
    private final TokenTextSplitter textSplitter;

    public RagDocumentIngestionService(
            VectorStore vectorStore,
            RagDocumentRepository documentRepository,
            RagDocumentVersionRepository versionRepository,
            RagFileStorageService fileStorageService) {

        this.vectorStore = vectorStore;
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.fileStorageService = fileStorageService;

        this.textSplitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(20)
                .withMaxNumChunks(5000)
                .withKeepSeparator(true)
                .build();
    }

    public DocumentIngestionResponseDTO ingestDocument(
            MultipartFile file) {

        validateFile(file);

        String documentId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String fileType = getFileExtension(fileName);

        String storagePath = null;
        boolean vectorsStored = false;

        try {
            byte[] fileBytes = file.getBytes();
            String checksum = calculateChecksum(fileBytes);

            if (documentRepository.findByChecksum(checksum).isPresent()) {
                throw new IllegalArgumentException(
                        "This document has already been uploaded.");
            }

            storagePath = fileStorageService.store(file, documentId);

            List<Document> extractedDocuments =
                    extractDocuments(fileBytes, fileName, documentId);

            if (extractedDocuments.isEmpty()) {
                throw new IllegalArgumentException(
                        "No readable content was found in the uploaded file.");
            }

            List<Document> chunks = textSplitter.apply(extractedDocuments);

            if (chunks.isEmpty()) {
                throw new IllegalArgumentException(
                        "Unable to generate document chunks.");
            }

            vectorStore.add(chunks);
            vectorsStored = true;

            Instant now = Instant.now();

            RagDocument document = new RagDocument();
            document.setDocumentId(documentId);
            document.setFileName(fileName);
            document.setFileType(fileType);
            document.setChunkCount(chunks.size());
            document.setStatus("COMPLETED");
            document.setUploadedAt(now);
            document.setUpdatedAt(now);
            document.setStoragePath(storagePath);
            document.setChecksum(checksum);
            document.setVersion(1);

            RagDocumentVersion initialVersion =
                    new RagDocumentVersion(
                            documentId,
                            1,
                            fileName,
                            storagePath,
                            checksum,
                            chunks.size(),
                            now
                    );

            saveDocumentAndVersion(document, initialVersion);

            return new DocumentIngestionResponseDTO(
                    documentId,
                    fileName,
                    "SUCCESS",
                    extractedDocuments.size(),
                    chunks.size(),
                    "Document uploaded, indexed and stored successfully."
            );

        } catch (Exception exception) {

            if (vectorsStored) {
                cleanupVectors(documentId, exception);
            }

            if (storagePath != null) {
                try {
                    fileStorageService.delete(storagePath);
                } catch (Exception cleanupException) {
                    exception.addSuppressed(cleanupException);
                }
            }

            if (exception instanceof IllegalArgumentException argumentException) {
                throw argumentException;
            }

            throw new IllegalStateException(
                    "Document ingestion failed.", exception);
        }
    }

    @Transactional
    protected void saveDocumentAndVersion(
            RagDocument document,
            RagDocumentVersion version) {

        documentRepository.save(document);
        versionRepository.save(version);
    }

    private List<Document> extractDocuments(
            byte[] fileBytes,
            String fileName,
            String documentId) {

        ByteArrayResource resource = new ByteArrayResource(fileBytes) {

            @Override
            public String getFilename() {
                return fileName;
            }
        };

        TikaDocumentReader reader = new TikaDocumentReader(resource);

        List<Document> extractedDocuments = reader.read();

        List<Document> enrichedDocuments = new ArrayList<>();

        for (int i = 0; i < extractedDocuments.size(); i++) {

            Document extracted = extractedDocuments.get(i);
            String content = extracted.getText();

            if (content == null || content.isBlank()) {
                continue;
            }

            Map<String, Object> metadata =
                    new HashMap<>(extracted.getMetadata());

            metadata.put("documentId", documentId);
            metadata.put("fileName", fileName);
            metadata.put("fileType", getFileExtension(fileName));
            metadata.put("uploadedAt", Instant.now().toString());
            metadata.put("sourceType", "USER_UPLOAD");
            metadata.put("documentPart", i + 1);

            enrichedDocuments.add(
                    new Document(content.trim(), metadata));
        }

        return enrichedDocuments;
    }

    private String calculateChecksum(byte[] content) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(content));

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable.", exception);
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please upload a non-empty document.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "File size cannot exceed 10 MB.");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()
                || fileName.contains("/")
                || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String extension = getFileExtension(fileName);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Only PDF, DOCX and TXT files are supported.");
        }
    }

    private String getFileExtension(String fileName) {

        int lastDot = fileName.lastIndexOf('.');

        if (lastDot < 0 || lastDot == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDot + 1)
                .toLowerCase(Locale.ROOT);
    }

    private void cleanupVectors(
            String documentId,
            Exception originalException) {

        try {
            FilterExpressionBuilder builder =
                    new FilterExpressionBuilder();

            vectorStore.delete(
                    builder.eq("documentId", documentId).build());

        } catch (Exception cleanupException) {
            originalException.addSuppressed(cleanupException);
        }
    }
}