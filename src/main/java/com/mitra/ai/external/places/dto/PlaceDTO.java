
package com.mitra.ai.external.places.dto;

import java.util.List;

public record PlaceDTO(

        String name,

        String category,

        String address,

        String city,

        String state,

        String country,

        Double latitude,

        Double longitude,

        Double distanceInMeters,

        String placeId,

        List<String> categories

) {
}
