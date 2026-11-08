package com.mitra.ai.intelligence.controller;

import com.mitra.ai.intelligence.dto.TripPlanningSummaryDTO;
import com.mitra.ai.intelligence.service.ItineraryIntelligenceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intelligence")
public class ItineraryIntelligenceController {

    private final ItineraryIntelligenceService intelligenceService;

    public ItineraryIntelligenceController(
            ItineraryIntelligenceService intelligenceService) {

        this.intelligenceService = intelligenceService;
    }

    @GetMapping("/trips/{tripId}/summary")
    public ResponseEntity<TripPlanningSummaryDTO> getTripPlanningSummary(
            @PathVariable Long tripId) {

        return ResponseEntity.ok(
                intelligenceService.generateTripPlanningSummary(tripId)
        );
    }
}