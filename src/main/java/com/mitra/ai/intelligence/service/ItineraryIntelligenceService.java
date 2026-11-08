package com.mitra.ai.intelligence.service;

import com.mitra.ai.entity.Activity;
import com.mitra.ai.entity.Budget;
import com.mitra.ai.entity.Expense;
import com.mitra.ai.entity.Itinerary;
import com.mitra.ai.entity.Trip;
import com.mitra.ai.intelligence.dto.ActivitySummaryDTO;
import com.mitra.ai.intelligence.dto.BudgetSummaryDTO;
import com.mitra.ai.intelligence.dto.DayItinerarySummaryDTO;
import com.mitra.ai.intelligence.dto.TripPlanningSummaryDTO;
import com.mitra.ai.repository.ActivityRepository;
import com.mitra.ai.repository.BudgetRepository;
import com.mitra.ai.repository.ExpenseRepository;
import com.mitra.ai.repository.ItineraryRepository;
import com.mitra.ai.repository.TripRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ItineraryIntelligenceService {

    private final TripRepository tripRepository;
    private final ItineraryRepository itineraryRepository;
    private final ActivityRepository activityRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public ItineraryIntelligenceService(
            TripRepository tripRepository,
            ItineraryRepository itineraryRepository,
            ActivityRepository activityRepository,
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository) {

        this.tripRepository = tripRepository;
        this.itineraryRepository = itineraryRepository;
        this.activityRepository = activityRepository;
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    @Transactional(readOnly = true)
    public TripPlanningSummaryDTO generateTripPlanningSummary(Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Trip not found"));

        List<Itinerary> itineraries =
                itineraryRepository.findByTripIdOrderByDayNumberAsc(tripId);

        List<DayItinerarySummaryDTO> itinerarySummary =
                itineraries.stream()
                        .map(this::convertItinerary)
                        .toList();

        long totalActivities =
                itinerarySummary.stream()
                        .mapToLong(day -> day.getActivities().size())
                        .sum();

        BudgetSummaryDTO budgetSummary =
                buildBudgetSummary(tripId);

        return new TripPlanningSummaryDTO(
                trip.getId(),
                trip.getTitle(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                itinerarySummary.size(),
                totalActivities,
                itinerarySummary,
                budgetSummary
        );
    }

    private DayItinerarySummaryDTO convertItinerary(
            Itinerary itinerary) {

        List<Activity> activities =
                activityRepository
                        .findByItineraryId(itinerary.getId());

        List<ActivitySummaryDTO> activitySummary =
                activities.stream()
                        .map(this::convertActivity)
                        .toList();

        return new DayItinerarySummaryDTO(
                itinerary.getId(),
                itinerary.getTrip().getId(),
                itinerary.getDate(),
                itinerary.getTitle(),
                itinerary.getDescription(),
                itinerary.getDayNumber(),
                activitySummary
        );
    }

    private ActivitySummaryDTO convertActivity(
            Activity activity) {

        return new ActivitySummaryDTO(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getLocation(),
                activity.getStartTime(),
                activity.getEndTime()
        );
    }

    private BudgetSummaryDTO buildBudgetSummary(Long tripId) {

        Budget budget = budgetRepository
                .findByTripId(tripId)
                .orElse(null);

        List<Expense> expenses =
                expenseRepository.findByTripId(tripId);

        BigDecimal totalExpenses =
                expenses.stream()
                        .map(Expense::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        if (budget == null) {

            return new BudgetSummaryDTO(
                    false,
                    BigDecimal.ZERO,
                    totalExpenses,
                    BigDecimal.ZERO.subtract(totalExpenses),
                    null,
                    0.0
            );
        }

        BigDecimal totalBudget = budget.getTotalBudget();

        BigDecimal remainingBudget =
                totalBudget.subtract(totalExpenses);

        double percentageUsed = 0.0;

        if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {

            percentageUsed =
                    totalExpenses
                            .divide(
                                    totalBudget,
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue();
        }

        return new BudgetSummaryDTO(
                true,
                totalBudget,
                totalExpenses,
                remainingBudget,
                budget.getCurrency(),
                percentageUsed
        );
    }
}