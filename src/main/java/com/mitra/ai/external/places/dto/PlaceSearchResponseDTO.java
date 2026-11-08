
package com.mitra.ai.external.places.dto;

import java.util.List;

public record PlaceSearchResponseDTO(

        String searchCategory,

        Double searchLatitude,

        Double searchLongitude,

        Integer searchRadiusMeters,

        Integer totalResults,

        List<PlaceDTO> places

) {
}
