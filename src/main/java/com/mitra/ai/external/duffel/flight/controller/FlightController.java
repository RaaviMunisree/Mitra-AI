package com.mitra.ai.external.duffel.flight.controller;

import com.mitra.ai.external.duffel.flight.dto.FlightSearchRequestDTO;
import com.mitra.ai.external.duffel.flight.dto.FlightSearchResponseDTO;
import com.mitra.ai.external.duffel.flight.service.DuffelFlightService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/flights")
public class FlightController {

    private final DuffelFlightService flightService;

    public FlightController(DuffelFlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> searchFlights(
            @Valid @RequestBody FlightSearchRequestDTO request) {

        return ResponseEntity.ok(
                flightService.searchFlights(request));
    }
}