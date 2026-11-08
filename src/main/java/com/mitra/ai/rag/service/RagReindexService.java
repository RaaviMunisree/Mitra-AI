package com.mitra.ai.rag.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitra.ai.rag.entity.RagDocument;
import com.mitra.ai.rag.repository.RagDocumentRepository;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
public class RagReindexService {

    private final RagDocumentRepository documentRepository;
    private final RagFileStorageService fileStorageService;
    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter;

    public RagReindexService(
            RagDocumentRepository documentRepository,
            RagFileStorageService fileStorageService,
            VectorStore vectorStore) {

        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.vectorStore = vectorStore;

        this.textSplitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(20)
                .withMaxNumChunks(5000)
                .withKeepSeparator(true)
                .build();
    }

    public int reindexDocument(String documentId) {

        RagDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Document not found: " + documentId));

        try {
            byte[] fileBytes =
                    fileStorageService.read(document.getStoragePath());

            ByteArrayResource resource =
                    new ByteArrayResource(fileBytes) {

                @Override
                public String getFilename() {
                    return document.getFileName();
                }
            };

            List<Document> extractedDocuments =
                    new TikaDocumentReader(resource).read();

            List<Document> enrichedDocuments = new ArrayList<>();

            for (int i = 0; i < extractedDocuments.size(); i++) {

                Document extracted = extractedDocuments.get(i);

                if (extracted.getText() == null
                        || extracted.getText().isBlank()) {
                    continue;
                }

                Map<String, Object> metadata =
                        new HashMap<>(extracted.getMetadata());

                metadata.put("documentId", documentId);
                metadata.put("fileName", document.getFileName());
                metadata.put("fileType", document.getFileType());
                metadata.put("uploadedAt",
                        document.getUploadedAt().toString());
                metadata.put("sourceType", "USER_UPLOAD");
                metadata.put("documentPart", i + 1);
                metadata.put("version", document.getVersion());

                enrichedDocuments.add(
                        new Document(extracted.getText().trim(), metadata));
            }

            List<Document> chunks = textSplitter.apply(enrichedDocuments);

            if (chunks.isEmpty()) {
                throw new IllegalStateException(
                        "No chunks could be generated during re-indexing.");
            }

            // Remove the previous indexed chunks.
            FilterExpressionBuilder builder =
                    new FilterExpressionBuilder();

            vectorStore.delete(
                    builder.eq("documentId", documentId).build());

            // Save the newly generated chunks.
            vectorStore.add(chunks);

            document.setChunkCount(chunks.size());
            document.setStatus("COMPLETED");
            document.setUpdatedAt(Instant.now());

            documentRepository.save(document);

            return chunks.size();

        } catch (Exception exception) {

            document.setStatus("REINDEX_FAILED");
            document.setUpdatedAt(Instant.now());
            documentRepository.save(document);

            throw new IllegalStateException(
                    "Unable to re-index document: " + documentId,
                    exception);
        }
    }
}