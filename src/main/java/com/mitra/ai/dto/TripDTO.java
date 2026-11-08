package com.mitra.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TripDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Trip title is required")
    @Size(
        min = 2,
        max = 150,
        message = "Title must be between 2 and 150 characters"
    )
    private String title;

    @NotBlank(message = "Destination is required")
    @Size(
        max = 150,
        message = "Destination cannot exceed 150 characters"
    )
    private String destination;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Size(
        max = 1000,
        message = "Description cannot exceed 1000 characters"
    )
    private String description;

    public TripDTO() {
    }

    public TripDTO(
            Long userId,
            String title,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            String description) {

        this.userId = userId;
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}