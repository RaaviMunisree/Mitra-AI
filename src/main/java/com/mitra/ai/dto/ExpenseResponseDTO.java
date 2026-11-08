package com.mitra.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponseDTO {

    private Long id;
    private Long tripId;
    private String category;
    private String description;
    private BigDecimal amount;
    private String currency;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExpenseResponseDTO() {
    }

    public ExpenseResponseDTO(
            Long id,
            Long tripId,
            String category,
            String description,
            BigDecimal amount,
            String currency,
            LocalDate expenseDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.tripId = tripId;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.expenseDate = expenseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}