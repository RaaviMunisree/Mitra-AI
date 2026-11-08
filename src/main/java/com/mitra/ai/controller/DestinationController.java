package com.mitra.ai.controller;

import com.mitra.ai.dto.DestinationDTO;
import com.mitra.ai.dto.DestinationResponseDTO;
import com.mitra.ai.entity.Destination;
import com.mitra.ai.service.DestinationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(
            DestinationService destinationService) {

        this.destinationService = destinationService;
    }

    // CREATE DESTINATION
    @PostMapping
    public ResponseEntity<DestinationResponseDTO>
    createDestination(
            @Valid @RequestBody DestinationDTO destinationDTO) {

        Destination destination = new Destination(
                destinationDTO.getName(),
                destinationDTO.getCountry(),
                destinationDTO.getState(),
                destinationDTO.getDescription()
        );

        Destination createdDestination =
                destinationService.createDestination(
                        destination);

        return new ResponseEntity<>(
                convertToResponseDTO(createdDestination),
                HttpStatus.CREATED
        );
    }

    // GET ALL DESTINATIONS
    @GetMapping
    public ResponseEntity<List<DestinationResponseDTO>>
    getAllDestinations() {

        List<DestinationResponseDTO> destinations =
                destinationService.getAllDestinations()
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return ResponseEntity.ok(destinations);
    }

    // GET DESTINATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponseDTO>
    getDestinationById(@PathVariable Long id) {

        return destinationService
                .getDestinationById(id)
                .map(destination ->
                        ResponseEntity.ok(
                                convertToResponseDTO(destination)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // GET DESTINATION BY NAME
    @GetMapping("/name/{name}")
    public ResponseEntity<DestinationResponseDTO>
    getDestinationByName(@PathVariable String name) {

        return destinationService
                .getDestinationByName(name)
                .map(destination ->
                        ResponseEntity.ok(
                                convertToResponseDTO(destination)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // GET DESTINATIONS BY COUNTRY
    @GetMapping("/country/{country}")
    public ResponseEntity<List<DestinationResponseDTO>>
    getDestinationsByCountry(
            @PathVariable String country) {

        List<DestinationResponseDTO> destinations =
                destinationService
                        .getDestinationsByCountry(country)
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return ResponseEntity.ok(destinations);
    }

    // GET DESTINATIONS BY STATE
    @GetMapping("/state/{state}")
    public ResponseEntity<List<DestinationResponseDTO>>
    getDestinationsByState(
            @PathVariable String state) {

        List<DestinationResponseDTO> destinations =
                destinationService
                        .getDestinationsByState(state)
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return ResponseEntity.ok(destinations);
    }

    // UPDATE DESTINATION
    @PutMapping("/{id}")
    public ResponseEntity<DestinationResponseDTO>
    updateDestination(
            @PathVariable Long id,
            @Valid @RequestBody DestinationDTO destinationDTO) {

        Destination updatedDestination = new Destination(
                destinationDTO.getName(),
                destinationDTO.getCountry(),
                destinationDTO.getState(),
                destinationDTO.getDescription()
        );

        Destination destination =
                destinationService.updateDestination(
                        id,
                        updatedDestination);

        return ResponseEntity.ok(
                convertToResponseDTO(destination));
    }

    // DELETE DESTINATION
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(
            @PathVariable Long id) {

        destinationService.deleteDestination(id);

        return ResponseEntity.noContent().build();
    }

    // ENTITY -> RESPONSE DTO
    private DestinationResponseDTO convertToResponseDTO(
            Destination destination) {

        return new DestinationResponseDTO(
                destination.getId(),
                destination.getName(),
                destination.getCountry(),
                destination.getState(),
                destination.getDescription(),
                destination.getCreatedAt(),
                destination.getUpdatedAt()
        );
    }
}