package com.mitra.ai.external.duffel.flight.dto;

import java.util.List;

public record FlightSearchResponseDTO(
        String offerRequestId,
        boolean liveMode,
        int totalOffers,
        List<FlightOfferDTO> offers
) {
}