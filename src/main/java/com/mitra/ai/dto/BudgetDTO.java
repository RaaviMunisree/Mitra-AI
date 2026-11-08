package com.mitra.ai.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BudgetDTO {

    @NotNull(message = "Trip ID is required")
    private Long tripId;

    @NotNull(message = "Total budget is required")
    @DecimalMin(value = "0.0",
            inclusive = false,
            message = "Budget must be greater than zero")
    private BigDecimal totalBudget;

    @NotBlank(message = "Currency is required")
    private String currency;

    public BudgetDTO() {
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}