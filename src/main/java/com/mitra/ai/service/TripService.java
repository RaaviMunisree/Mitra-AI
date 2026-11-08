package com.mitra.ai.service;

import com.mitra.ai.entity.Trip;
import com.mitra.ai.entity.User;
import com.mitra.ai.repository.TripRepository;
import com.mitra.ai.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(
            TripRepository tripRepository,
            UserRepository userRepository) {

        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public Trip createTrip(Trip trip, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        validateDates(trip);

        trip.setUser(user);

        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    public List<Trip> getTripsByDestination(String destination) {
        return tripRepository.findByDestinationIgnoreCase(destination);
    }

    public List<Trip> getTripsByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        return tripRepository.findByUserId(userId);
    }

    public Trip updateTrip(
            Long id,
            Trip updatedTrip,
            Long userId) {

        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Trip not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        validateDates(updatedTrip);

        existingTrip.setUser(user);
        existingTrip.setTitle(updatedTrip.getTitle());
        existingTrip.setDestination(updatedTrip.getDestination());
        existingTrip.setStartDate(updatedTrip.getStartDate());
        existingTrip.setEndDate(updatedTrip.getEndDate());
        existingTrip.setDescription(updatedTrip.getDescription());

        return tripRepository.save(existingTrip);
    }

    public void deleteTrip(Long id) {

        if (!tripRepository.existsById(id)) {
            throw new IllegalArgumentException("Trip not found");
        }

        tripRepository.deleteById(id);
    }

    private void validateDates(Trip trip) {

        if (trip.getStartDate() == null ||
                trip.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required");
        }

        if (trip.getEndDate().isBefore(trip.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }
    }
}