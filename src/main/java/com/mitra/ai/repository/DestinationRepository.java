package com.mitra.ai.repository;

import com.mitra.ai.entity.Destination;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    Optional<Destination> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Destination> findByCountryIgnoreCase(String country);

    List<Destination> findByStateIgnoreCase(String state);
}