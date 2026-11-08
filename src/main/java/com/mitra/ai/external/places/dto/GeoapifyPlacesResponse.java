
package com.mitra.ai.external.places.dto;

import java.util.List;

public record GeoapifyPlacesResponse(

        String type,

        List<Feature> features

) {

    public record Feature(

            String type,

            Properties properties

    ) {
    }

    public record Properties(

            String name,

            String country,

            String state,

            String city,

            String postcode,

            String street,

            String housenumber,

            String formatted,

            String address_line1,

            String address_line2,

            Double lat,

            Double lon,

            Double distance,

            String place_id,

            List<String> categories

    ) {
    }
}
