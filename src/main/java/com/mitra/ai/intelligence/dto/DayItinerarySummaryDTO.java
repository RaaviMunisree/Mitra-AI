package com.mitra.ai.intelligence.dto;

import java.time.LocalDate;
import java.util.List;

public class DayItinerarySummaryDTO {

    private Long itineraryId;
    private Long tripId;
    private LocalDate date;
    private String title;
    private String description;
    private Integer dayNumber;
    private List<ActivitySummaryDTO> activities;

    public DayItinerarySummaryDTO() {
    }

    public DayItinerarySummaryDTO(
            Long itineraryId,
            Long tripId,
            LocalDate date,
            String title,
            String description,
            Integer dayNumber,
            List<ActivitySummaryDTO> activities) {

        this.itineraryId = itineraryId;
        this.tripId = tripId;
        this.date = date;
        this.title = title;
        this.description = description;
        this.dayNumber = dayNumber;
        this.activities = activities;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public List<ActivitySummaryDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitySummaryDTO> activities) {
        this.activities = activities;
    }
}