
package com.mitra.ai.ai.service;

import java.util.List;

import com.mitra.ai.ai.dto.AiChatResponseDTO;
import com.mitra.ai.ai.prompt.MitraPromptTemplates;
import com.mitra.ai.intelligence.dto.TripPlanningSummaryDTO;
import com.mitra.ai.intelligence.service.ItineraryIntelligenceService;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
public class MitraAiChatService {

    private final ChatClient chatClient;
    private final ItineraryIntelligenceService intelligenceService;
    private final JsonMapper jsonMapper;

    public MitraAiChatService(
            ChatClient.Builder chatClientBuilder,
            ItineraryIntelligenceService intelligenceService,
            JsonMapper jsonMapper,
            ChatMemory chatMemory) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        this.intelligenceService = intelligenceService;
        this.jsonMapper = jsonMapper;
    }

    // General AI chat with structured responses and conversation memory.
    public AiChatResponseDTO generateResponse(
            String conversationId,
            String prompt) {

        AiChatResponseDTO response = chatClient.prompt()
                .system(MitraPromptTemplates.GENERAL_SYSTEM_PROMPT)
                .user(prompt)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        "general:" + conversationId
                ))
                .call()
                .entity(AiChatResponseDTO.class);

        return normalizeResponse(response);
    }

    // Trip-specific AI chat with structured responses and conversation memory.
    public AiChatResponseDTO generateTripResponse(
            Long tripId,
            String conversationId,
            String prompt) {

        // Step 1: Retrieve existing trip information.
        TripPlanningSummaryDTO tripSummary =
                intelligenceService.generateTripPlanningSummary(tripId);

        // Step 2: Convert trip information into JSON.
        String tripContext;

        try {
            tripContext = jsonMapper.writeValueAsString(tripSummary);
        } catch (JacksonException exception) {
            throw new IllegalStateException(
                    "Unable to serialize trip information.",
                    exception
            );
        }

        // Step 3: Include current trip context in the system instructions.
        String systemPrompt = """
                %s

                Current trip information in JSON format:

                %s
                """.formatted(
                        MitraPromptTemplates.TRIP_SYSTEM_PROMPT,
                        tripContext
                );

        // Step 4: Generate a structured response with conversation memory.
        AiChatResponseDTO response = chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        "trip:" + tripId + ":" + conversationId
                ))
                .call()
                .entity(AiChatResponseDTO.class);

        return normalizeResponse(response);
    }

    // Ensure the API receives consistent response fields.
    private AiChatResponseDTO normalizeResponse(
            AiChatResponseDTO response) {

        if (response == null) {
            throw new IllegalStateException(
                    "AI service returned an empty response."
            );
        }

        if (response.getAnswer() == null
                || response.getAnswer().isBlank()) {

            throw new IllegalStateException(
                    "AI service returned an empty answer."
            );
        }

        if (response.getHighlights() == null) {
            response.setHighlights(List.of());
        }

        if (response.getRecommendations() == null) {
            response.setRecommendations(List.of());
        }

        if (response.getEstimatedCosts() == null) {
            response.setEstimatedCosts(List.of());
        }

        if (response.getFollowUpQuestions() == null) {
            response.setFollowUpQuestions(List.of());
        }

        return response;
    }
}
