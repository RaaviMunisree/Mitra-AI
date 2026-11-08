package com.mitra.ai.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDTO {

    @NotNull(message = "Trip ID is required")
    private Long tripId;

    @NotBlank(message = "Category is required")
    @Size(max = 150)
    private String category;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0",
            inclusive = false,
            message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 10)
    private String currency;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    public ExpenseDTO() {
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
}