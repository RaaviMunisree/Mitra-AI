package com.mitra.ai.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ItineraryResponseDTO {

    private Long id;
    private Long tripId;
    private LocalDate date;
    private String title;
    private String description;
    private Integer dayNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItineraryResponseDTO() {
    }

    public ItineraryResponseDTO(
            Long id,
            Long tripId,
            LocalDate date,
            String title,
            String description,
            Integer dayNumber,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.tripId = tripId;
        this.date = date;
        this.title = title;
        this.description = description;
        this.dayNumber = dayNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}