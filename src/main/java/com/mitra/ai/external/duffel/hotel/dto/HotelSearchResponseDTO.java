package com.mitra.ai.external.duffel.hotel.dto;

import java.util.List;

public record HotelSearchResponseDTO(
        int totalResults,
        List<HotelDTO> hotels
) {
}