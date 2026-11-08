package com.mitra.ai.repository;

import com.mitra.ai.entity.Itinerary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItineraryRepository
        extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByTripId(Long tripId);

    List<Itinerary> findByTripIdAndDate(
            Long tripId,
            LocalDate date);

    List<Itinerary> findByTripIdOrderByDayNumberAsc(
            Long tripId);
}