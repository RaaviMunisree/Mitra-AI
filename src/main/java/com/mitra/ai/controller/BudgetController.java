package com.mitra.ai.controller;

import com.mitra.ai.dto.BudgetDTO;
import com.mitra.ai.dto.BudgetResponseDTO;
import com.mitra.ai.entity.Budget;
import com.mitra.ai.service.BudgetService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDTO>
    createBudget(
            @Valid @RequestBody BudgetDTO dto) {

        Budget budget = new Budget(
                dto.getTotalBudget(),
                dto.getCurrency()
        );

        Budget created =
                budgetService.createBudget(
                        budget,
                        dto.getTripId());

        return new ResponseEntity<>(
                toDTO(created),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>>
    getAllBudgets() {

        return ResponseEntity.ok(
                budgetService.getAllBudgets()
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO>
    getBudgetById(@PathVariable Long id) {

        return budgetService.getBudgetById(id)
                .map(budget ->
                        ResponseEntity.ok(toDTO(budget)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<BudgetResponseDTO>
    getBudgetByTrip(@PathVariable Long tripId) {

        return budgetService.getBudgetByTrip(tripId)
                .map(budget ->
                        ResponseEntity.ok(toDTO(budget)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO>
    updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetDTO dto) {

        Budget updated = new Budget(
                dto.getTotalBudget(),
                dto.getCurrency()
        );

        Budget result =
                budgetService.updateBudget(
                        id,
                        updated,
                        dto.getTripId());

        return ResponseEntity.ok(toDTO(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id) {

        budgetService.deleteBudget(id);

        return ResponseEntity.noContent().build();
    }

    private BudgetResponseDTO toDTO(Budget budget) {

        return new BudgetResponseDTO(
                budget.getId(),
                budget.getTrip().getId(),
                budget.getTotalBudget(),
                budget.getCurrency(),
                budget.getCreatedAt(),
                budget.getUpdatedAt()
        );
    }
}