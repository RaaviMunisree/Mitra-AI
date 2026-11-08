
package com.mitra.ai.external.places.dto;

import java.util.List;

public record GeoapifyPlacesRequest(

        List<String> categories,

        SpatialFilter filter,

        SpatialBias bias,

        Integer limit,

        Integer offset,

        String lang

) {

    public record SpatialFilter(
            String type,
            Double lon,
            Double lat,
            Integer radius
    ) {
    }

    public record SpatialBias(
            String type,
            Double lon,
            Double lat
    ) {
    }
}
