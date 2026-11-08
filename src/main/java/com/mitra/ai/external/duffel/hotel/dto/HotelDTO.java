package com.mitra.ai.external.duffel.hotel.dto;

public record HotelDTO(
        String accommodationId,
        String name,
        String description,
        String address,
        String city,
        String countryCode,
        Double latitude,
        Double longitude,
        Integer starRating,
        Double reviewScore,
        Integer reviewCount,
        String cheapestRateAmount,
        String currency,
        String checkInDate,
        String checkOutDate,
        String expiresAt
) {
}