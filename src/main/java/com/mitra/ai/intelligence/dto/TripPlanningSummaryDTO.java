package com.mitra.ai.intelligence.dto;

import java.time.LocalDate;
import java.util.List;

public class TripPlanningSummaryDTO {

    private Long tripId;
    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;

    private long totalItineraryDays;
    private long totalActivities;

    private List<DayItinerarySummaryDTO> itinerary;

    private BudgetSummaryDTO budget;

    public TripPlanningSummaryDTO() {
    }

    public TripPlanningSummaryDTO(
            Long tripId,
            String title,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            long totalItineraryDays,
            long totalActivities,
            List<DayItinerarySummaryDTO> itinerary,
            BudgetSummaryDTO budget) {

        this.tripId = tripId;
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalItineraryDays = totalItineraryDays;
        this.totalActivities = totalActivities;
        this.itinerary = itinerary;
        this.budget = budget;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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

    public long getTotalItineraryDays() {
        return totalItineraryDays;
    }

    public void setTotalItineraryDays(long totalItineraryDays) {
        this.totalItineraryDays = totalItineraryDays;
    }

    public long getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(long totalActivities) {
        this.totalActivities = totalActivities;
    }

    public List<DayItinerarySummaryDTO> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<DayItinerarySummaryDTO> itinerary) {
        this.itinerary = itinerary;
    }

    public BudgetSummaryDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetSummaryDTO budget) {
        this.budget = budget;
    }
}