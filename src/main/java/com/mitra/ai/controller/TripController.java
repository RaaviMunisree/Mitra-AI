package com.mitra.ai.controller;

import com.mitra.ai.dto.TripDTO;
import com.mitra.ai.dto.TripResponseDTO;
import com.mitra.ai.entity.Trip;
import com.mitra.ai.service.TripService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // CREATE TRIP
    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(
            @Valid @RequestBody TripDTO tripDTO) {

        Trip trip = new Trip(
                tripDTO.getTitle(),
                tripDTO.getDestination(),
                tripDTO.getStartDate(),
                tripDTO.getEndDate(),
                tripDTO.getDescription()
        );

        Trip createdTrip =
                tripService.createTrip(trip, tripDTO.getUserId());

        return new ResponseEntity<>(
                convertToResponseDTO(createdTrip),
                HttpStatus.CREATED
        );
    }

    // GET ALL TRIPS
    @GetMapping
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {

        List<TripResponseDTO> trips =
                tripService.getAllTrips()
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return ResponseEntity.ok(trips);
    }

    // GET TRIP BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTripById(
            @PathVariable Long id) {

        return tripService.getTripById(id)
                .map(trip ->
                        ResponseEntity.ok(
                                convertToResponseDTO(trip)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // GET TRIPS BY DESTINATION
    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<TripResponseDTO>>
    getTripsByDestination(
            @PathVariable String destination) {

        List<TripResponseDTO> trips =
                tripService.getTripsByDestination(destination)
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return ResponseEntity.ok(trips);
    }

    // GET TRIPS BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TripResponseDTO>>
    getTripsByUser(@PathVariable Long userId) {

        List<TripResponseDTO> trips =
                tripService.getTripsByUser(userId)
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return ResponseEntity.ok(trips);
    }

    // UPDATE TRIP
    @PutMapping("/{id}")
    public ResponseEntity<TripResponseDTO> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripDTO tripDTO) {

        Trip updatedTrip = new Trip(
                tripDTO.getTitle(),
                tripDTO.getDestination(),
                tripDTO.getStartDate(),
                tripDTO.getEndDate(),
                tripDTO.getDescription()
        );

        Trip trip = tripService.updateTrip(
                id,
                updatedTrip,
                tripDTO.getUserId()
        );

        return ResponseEntity.ok(
                convertToResponseDTO(trip)
        );
    }

    // DELETE TRIP
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id) {

        tripService.deleteTrip(id);

        return ResponseEntity.noContent().build();
    }

    // ENTITY -> RESPONSE DTO
    private TripResponseDTO convertToResponseDTO(Trip trip) {

        return new TripResponseDTO(
                trip.getId(),
                trip.getUser().getId(),
                trip.getTitle(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getDescription(),
                trip.getCreatedAt(),
                trip.getUpdatedAt()
        );
    }
}