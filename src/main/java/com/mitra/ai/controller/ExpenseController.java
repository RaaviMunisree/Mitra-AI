package com.mitra.ai.controller;

import com.mitra.ai.dto.ExpenseDTO;
import com.mitra.ai.dto.ExpenseResponseDTO;
import com.mitra.ai.entity.Expense;
import com.mitra.ai.service.ExpenseService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(
            ExpenseService expenseService) {

        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO>
    createExpense(
            @Valid @RequestBody ExpenseDTO dto) {

        Expense expense = new Expense(
                dto.getCategory(),
                dto.getDescription(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getExpenseDate()
        );

        Expense created =
                expenseService.createExpense(
                        expense,
                        dto.getTripId());

        return new ResponseEntity<>(
                toDTO(created),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>>
    getAllExpenses() {

        return ResponseEntity.ok(
                expenseService.getAllExpenses()
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO>
    getExpenseById(@PathVariable Long id) {

        return expenseService.getExpenseById(id)
                .map(expense ->
                        ResponseEntity.ok(toDTO(expense)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ExpenseResponseDTO>>
    getExpensesByTrip(@PathVariable Long tripId) {

        return ResponseEntity.ok(
                expenseService
                        .getExpensesByTrip(tripId)
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @GetMapping("/trip/{tripId}/category/{category}")
    public ResponseEntity<List<ExpenseResponseDTO>>
    getExpensesByCategory(
            @PathVariable Long tripId,
            @PathVariable String category) {

        return ResponseEntity.ok(
                expenseService
                        .getExpensesByCategory(
                                tripId,
                                category)
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO>
    updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDTO dto) {

        Expense updated = new Expense(
                dto.getCategory(),
                dto.getDescription(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getExpenseDate()
        );

        Expense result =
                expenseService.updateExpense(
                        id,
                        updated,
                        dto.getTripId());

        return ResponseEntity.ok(toDTO(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }

    private ExpenseResponseDTO toDTO(
            Expense expense) {

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getTrip().getId(),
                expense.getCategory(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getExpenseDate(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }
}