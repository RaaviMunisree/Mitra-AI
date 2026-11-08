package com.mitra.ai.ai.dto;

import java.util.List;

public class AiChatResponseDTO {

    private String answer;
    private List<String> highlights;
    private List<String> recommendations;
    private List<String> estimatedCosts;
    private List<String> followUpQuestions;

    public AiChatResponseDTO() {
    }

    public AiChatResponseDTO(
            String answer,
            List<String> highlights,
            List<String> recommendations,
            List<String> estimatedCosts,
            List<String> followUpQuestions) {

        this.answer = answer;
        this.highlights = highlights;
        this.recommendations = recommendations;
        this.estimatedCosts = estimatedCosts;
        this.followUpQuestions = followUpQuestions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getEstimatedCosts() {
        return estimatedCosts;
    }

    public void setEstimatedCosts(List<String> estimatedCosts) {
        this.estimatedCosts = estimatedCosts;
    }

    public List<String> getFollowUpQuestions() {
        return followUpQuestions;
    }

    public void setFollowUpQuestions(List<String> followUpQuestions) {
        this.followUpQuestions = followUpQuestions;
    }
}