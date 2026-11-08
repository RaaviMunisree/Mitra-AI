package com.mitra.ai.service;

import com.mitra.ai.entity.Itinerary;
import com.mitra.ai.entity.Trip;
import com.mitra.ai.repository.ItineraryRepository;
import com.mitra.ai.repository.TripRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;

    public ItineraryService(
            ItineraryRepository itineraryRepository,
            TripRepository tripRepository) {

        this.itineraryRepository = itineraryRepository;
        this.tripRepository = tripRepository;
    }

    public Itinerary createItinerary(
            Itinerary itinerary,
            Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"));

        validateDate(itinerary, trip);

        itinerary.setTrip(trip);

        return itineraryRepository.save(itinerary);
    }

    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }

    public Optional<Itinerary> getItineraryById(Long id) {
        return itineraryRepository.findById(id);
    }

    public List<Itinerary> getItinerariesByTrip(Long tripId) {

        if (!tripRepository.existsById(tripId)) {
            throw new IllegalArgumentException("Trip not found");
        }

        return itineraryRepository
                .findByTripIdOrderByDayNumberAsc(tripId);
    }

    public Itinerary updateItinerary(
            Long id,
            Itinerary updated,
            Long tripId) {

        Itinerary existing =
                itineraryRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Itinerary not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"));

        validateDate(updated, trip);

        existing.setTrip(trip);
        existing.setDate(updated.getDate());
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setDayNumber(updated.getDayNumber());

        return itineraryRepository.save(existing);
    }

    public void deleteItinerary(Long id) {

        if (!itineraryRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Itinerary not found");
        }

        itineraryRepository.deleteById(id);
    }

    private void validateDate(
            Itinerary itinerary,
            Trip trip) {

        if (itinerary.getDate().isBefore(trip.getStartDate())
                || itinerary.getDate().isAfter(trip.getEndDate())) {

            throw new IllegalArgumentException(
                    "Itinerary date must be within the trip dates");
        }
    }
}