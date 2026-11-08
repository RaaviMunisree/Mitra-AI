package com.mitra.ai.external.duffel.hotel.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

public record HotelSearchRequestDTO(

        @NotNull
        @FutureOrPresent
        LocalDate checkInDate,

        @NotNull
        LocalDate checkOutDate,

        @NotNull
        @Min(1)
        @Max(10)
        Integer adults,

        @NotNull
        @Min(1)
        @Max(10)
        Integer rooms,

        @NotNull
        Double latitude,

        @NotNull
        Double longitude,

        @NotNull
        @Min(1)
        @Max(50)
        Integer radiusKm,

        Boolean freeCancellationOnly
) {
}