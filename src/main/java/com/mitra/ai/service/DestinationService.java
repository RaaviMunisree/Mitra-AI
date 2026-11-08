package com.mitra.ai.service;

import com.mitra.ai.entity.Destination;
import com.mitra.ai.repository.DestinationRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(
            DestinationRepository destinationRepository) {

        this.destinationRepository = destinationRepository;
    }

    public Destination createDestination(
            Destination destination) {

        if (destinationRepository
                .existsByNameIgnoreCase(destination.getName())) {

            throw new IllegalArgumentException(
                    "Destination already exists");
        }

        return destinationRepository.save(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public Optional<Destination> getDestinationByName(
            String name) {

        return destinationRepository.findByNameIgnoreCase(name);
    }

    public List<Destination> getDestinationsByCountry(
            String country) {

        return destinationRepository
                .findByCountryIgnoreCase(country);
    }

    public List<Destination> getDestinationsByState(
            String state) {

        return destinationRepository
                .findByStateIgnoreCase(state);
    }

    public Destination updateDestination(
            Long id,
            Destination updatedDestination) {

        Destination existingDestination =
                destinationRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Destination not found"));

        Optional<Destination> destinationWithSameName =
                destinationRepository
                        .findByNameIgnoreCase(
                                updatedDestination.getName());

        if (destinationWithSameName.isPresent()
                && !destinationWithSameName.get()
                        .getId().equals(id)) {

            throw new IllegalArgumentException(
                    "Another destination with this name already exists");
        }

        existingDestination.setName(
                updatedDestination.getName());

        existingDestination.setCountry(
                updatedDestination.getCountry());

        existingDestination.setState(
                updatedDestination.getState());

        existingDestination.setDescription(
                updatedDestination.getDescription());

        return destinationRepository.save(
                existingDestination);
    }

    public void deleteDestination(Long id) {

        if (!destinationRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "Destination not found");
        }

        destinationRepository.deleteById(id);
    }
}