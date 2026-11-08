package com.mitra.ai.external.duffel.flight.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;

public record FlightSearchRequestDTO(

        @NotBlank
        @Pattern(regexp = "^[A-Za-z]{3}$",
                message = "Origin must be a 3-letter IATA code")
        String origin,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z]{3}$",
                message = "Destination must be a 3-letter IATA code")
        String destination,

        @NotNull
        @FutureOrPresent
        LocalDate departureDate,

        LocalDate returnDate,

        @NotNull
        @Min(1)
        @Max(9)
        Integer adults,

        @NotBlank
        @Pattern(regexp = "^(economy|premium_economy|business|first)$")
        String cabinClass,

        @Min(0)
        @Max(2)
        Integer maxConnections
) {
}