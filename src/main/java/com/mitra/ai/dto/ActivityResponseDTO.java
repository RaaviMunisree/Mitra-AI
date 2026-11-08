package com.mitra.ai.dto;

import java.time.LocalDateTime;

public class ActivityResponseDTO {

    private Long id;
    private Long itineraryId;
    private String name;
    private String description;
    private String location;
    private String startTime;
    private String endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ActivityResponseDTO() {
    }

    public ActivityResponseDTO(
            Long id,
            Long itineraryId,
            String name,
            String description,
            String location,
            String startTime,
            String endTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.itineraryId = itineraryId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}