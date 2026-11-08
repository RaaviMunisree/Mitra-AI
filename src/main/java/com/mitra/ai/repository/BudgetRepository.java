package com.mitra.ai.repository;

import com.mitra.ai.entity.Budget;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository
        extends JpaRepository<Budget, Long> {

    Optional<Budget> findByTripId(Long tripId);

    boolean existsByTripId(Long tripId);
}