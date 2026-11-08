package com.mitra.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BudgetResponseDTO {

    private Long id;
    private Long tripId;
    private BigDecimal totalBudget;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BudgetResponseDTO() {
    }

    public BudgetResponseDTO(
            Long id,
            Long tripId,
            BigDecimal totalBudget,
            String currency,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.tripId = tripId;
        this.totalBudget = totalBudget;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}