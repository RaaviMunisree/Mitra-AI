package com.mitra.ai.service;

import com.mitra.ai.entity.Expense;
import com.mitra.ai.entity.Trip;
import com.mitra.ai.repository.ExpenseRepository;
import com.mitra.ai.repository.TripRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            TripRepository tripRepository) {

        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
    }

    public Expense createExpense(
            Expense expense,
            Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"));

        validateExpenseDate(expense, trip);

        expense.setTrip(trip);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public List<Expense> getExpensesByTrip(Long tripId) {

        if (!tripRepository.existsById(tripId)) {
            throw new IllegalArgumentException(
                    "Trip not found");
        }

        return expenseRepository.findByTripId(tripId);
    }

    public List<Expense> getExpensesByCategory(
            Long tripId,
            String category) {

        return expenseRepository
                .findByTripIdAndCategoryIgnoreCase(
                        tripId,
                        category);
    }

    public Expense updateExpense(
            Long id,
            Expense updated,
            Long tripId) {

        Expense existing =
                expenseRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Expense not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"));

        validateExpenseDate(updated, trip);

        existing.setTrip(trip);
        existing.setCategory(updated.getCategory());
        existing.setDescription(updated.getDescription());
        existing.setAmount(updated.getAmount());
        existing.setCurrency(updated.getCurrency());
        existing.setExpenseDate(updated.getExpenseDate());

        return expenseRepository.save(existing);
    }

    public void deleteExpense(Long id) {

        if (!expenseRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Expense not found");
        }

        expenseRepository.deleteById(id);
    }

    private void validateExpenseDate(
            Expense expense,
            Trip trip) {

        if (expense.getExpenseDate()
                .isBefore(trip.getStartDate())
                || expense.getExpenseDate()
                .isAfter(trip.getEndDate())) {

            throw new IllegalArgumentException(
                    "Expense date must be within the trip dates");
        }
    }
}