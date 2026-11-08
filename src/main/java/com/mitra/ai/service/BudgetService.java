package com.mitra.ai.service;

import com.mitra.ai.entity.Budget;
import com.mitra.ai.entity.Trip;
import com.mitra.ai.repository.BudgetRepository;
import com.mitra.ai.repository.TripRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TripRepository tripRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            TripRepository tripRepository) {

        this.budgetRepository = budgetRepository;
        this.tripRepository = tripRepository;
    }

    public Budget createBudget(
            Budget budget,
            Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"));

        if (budgetRepository.existsByTripId(tripId)) {
            throw new IllegalArgumentException(
                    "Budget already exists for this trip");
        }

        budget.setTrip(trip);

        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    public Optional<Budget> getBudgetByTrip(Long tripId) {

        return budgetRepository.findByTripId(tripId);
    }

    public Budget updateBudget(
            Long id,
            Budget updated,
            Long tripId) {

        Budget existing =
                budgetRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Budget not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"));

        existing.setTrip(trip);
        existing.setTotalBudget(
                updated.getTotalBudget());
        existing.setCurrency(
                updated.getCurrency());

        return budgetRepository.save(existing);
    }

    public void deleteBudget(Long id) {

        if (!budgetRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Budget not found");
        }

        budgetRepository.deleteById(id);
    }
}