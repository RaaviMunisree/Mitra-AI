package com.mitra.ai.dto;

import java.time.LocalDateTime;

public class DestinationResponseDTO {

    private Long id;
    private String name;
    private String country;
    private String state;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DestinationResponseDTO() {
    }

    public DestinationResponseDTO(
            Long id,
            String name,
            String country,
            String state,
            String description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.name = name;
        this.country = country;
        this.state = state;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}