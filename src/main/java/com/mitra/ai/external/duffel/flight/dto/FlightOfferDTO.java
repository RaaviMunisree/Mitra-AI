package com.mitra.ai.external.duffel.flight.dto;

import java.util.List;

public record FlightOfferDTO(
        String offerId,
        String airline,
        String totalAmount,
        String currency,
        String expiresAt,
        List<FlightSegmentDTO> segments
) {
    public record FlightSegmentDTO(
            String airline,
            String flightNumber,
            String origin,
            String destination,
            String departureTime,
            String arrivalTime
    ) {
    }
}