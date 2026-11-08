package com.mitra.ai.intelligence.dto;

import java.math.BigDecimal;

public class BudgetSummaryDTO {

    private boolean budgetConfigured;
    private BigDecimal totalBudget;
    private BigDecimal totalExpenses;
    private BigDecimal remainingBudget;
    private String currency;
    private double percentageUsed;

    public BudgetSummaryDTO() {
    }

    public BudgetSummaryDTO(
            boolean budgetConfigured,
            BigDecimal totalBudget,
            BigDecimal totalExpenses,
            BigDecimal remainingBudget,
            String currency,
            double percentageUsed) {

        this.budgetConfigured = budgetConfigured;
        this.totalBudget = totalBudget;
        this.totalExpenses = totalExpenses;
        this.remainingBudget = remainingBudget;
        this.currency = currency;
        this.percentageUsed = percentageUsed;
    }

    public boolean isBudgetConfigured() {
        return budgetConfigured;
    }

    public void setBudgetConfigured(boolean budgetConfigured) {
        this.budgetConfigured = budgetConfigured;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(BigDecimal remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPercentageUsed() {
        return percentageUsed;
    }

    public void setPercentageUsed(double percentageUsed) {
        this.percentageUsed = percentageUsed;
    }
}