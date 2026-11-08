package com.mitra.ai.repository;

import com.mitra.ai.entity.Expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByTripId(Long tripId);

    List<Expense> findByTripIdAndCategoryIgnoreCase(
            Long tripId,
            String category);
}