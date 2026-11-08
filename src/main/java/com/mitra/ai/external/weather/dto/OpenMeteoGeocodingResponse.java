
package com.mitra.ai.external.weather.dto;

import java.util.List;

public record OpenMeteoGeocodingResponse(

        List<LocationResult> results

) {

    public record LocationResult(

            Long id,
            String name,
            Double latitude,
            Double longitude,
            String country,
            String country_code,
            String admin1,
            String timezone,
            Long population

    ) {
    }
}
