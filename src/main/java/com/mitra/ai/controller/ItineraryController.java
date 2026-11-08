package com.mitra.ai.controller;

import com.mitra.ai.dto.ItineraryDTO;
import com.mitra.ai.dto.ItineraryResponseDTO;
import com.mitra.ai.entity.Itinerary;
import com.mitra.ai.service.ItineraryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(
            ItineraryService itineraryService) {

        this.itineraryService = itineraryService;
    }

    @PostMapping
    public ResponseEntity<ItineraryResponseDTO>
    createItinerary(
            @Valid @RequestBody ItineraryDTO dto) {

        Itinerary itinerary = new Itinerary(
                dto.getDate(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getDayNumber()
        );

        Itinerary created =
                itineraryService.createItinerary(
                        itinerary,
                        dto.getTripId());

        return new ResponseEntity<>(
                toDTO(created),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItineraryResponseDTO>>
    getAllItineraries() {

        return ResponseEntity.ok(
                itineraryService.getAllItineraries()
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItineraryResponseDTO>
    getItineraryById(@PathVariable Long id) {

        return itineraryService
                .getItineraryById(id)
                .map(itinerary ->
                        ResponseEntity.ok(toDTO(itinerary)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ItineraryResponseDTO>>
    getByTrip(@PathVariable Long tripId) {

        return ResponseEntity.ok(
                itineraryService
                        .getItinerariesByTrip(tripId)
                        .stream()
                        .map(this::toDTO)
                        .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItineraryResponseDTO>
    updateItinerary(
            @PathVariable Long id,
            @Valid @RequestBody ItineraryDTO dto) {

        Itinerary updated = new Itinerary(
                dto.getDate(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getDayNumber()
        );

        Itinerary result =
                itineraryService.updateItinerary(
                        id,
                        updated,
                        dto.getTripId());

        return ResponseEntity.ok(toDTO(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(
            @PathVariable Long id) {

        itineraryService.deleteItinerary(id);

        return ResponseEntity.noContent().build();
    }

    private ItineraryResponseDTO toDTO(
            Itinerary itinerary) {

        return new ItineraryResponseDTO(
                itinerary.getId(),
                itinerary.getTrip().getId(),
                itinerary.getDate(),
                itinerary.getTitle(),
                itinerary.getDescription(),
                itinerary.getDayNumber(),
                itinerary.getCreatedAt(),
                itinerary.getUpdatedAt()
        );
    }
}