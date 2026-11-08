
package com.mitra.ai.external.places.service;

import java.util.List;
import java.util.Objects;

import com.mitra.ai.external.places.dto.GeoapifyPlacesRequest;
import com.mitra.ai.external.places.dto.GeoapifyPlacesRequest.SpatialBias;
import com.mitra.ai.external.places.dto.GeoapifyPlacesRequest.SpatialFilter;
import com.mitra.ai.external.places.dto.GeoapifyPlacesResponse;
import com.mitra.ai.external.places.dto.PlaceCategory;
import com.mitra.ai.external.places.dto.PlaceDTO;
import com.mitra.ai.external.places.dto.PlaceSearchResponseDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GeoapifyPlacesService {

    private static final int DEFAULT_RADIUS = 5000;
    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;

    private final RestClient geoapifyRestClient;

    public GeoapifyPlacesService(
            @Qualifier("geoapifyRestClient")
            RestClient geoapifyRestClient) {

        this.geoapifyRestClient = geoapifyRestClient;
    }

    public PlaceSearchResponseDTO searchNearbyPlaces(
            PlaceCategory category,
            double latitude,
            double longitude,
            Integer radiusMeters,
            Integer requestedLimit) {

        // Step 1: Validate input.
        validateCoordinates(latitude, longitude);

        if (category == null) {
            throw new IllegalArgumentException(
                    "Place category is required."
            );
        }

        int radius = radiusMeters == null
                ? DEFAULT_RADIUS
                : radiusMeters;

        int limit = requestedLimit == null
                ? DEFAULT_LIMIT
                : requestedLimit;

        if (radius < 100 || radius > 50000) {
            throw new IllegalArgumentException(
                    "Radius must be between 100 and 50000 metres."
            );
        }

        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and " + MAX_LIMIT + "."
            );
        }

        // Step 2: Construct geographic filter.
        SpatialFilter filter = new SpatialFilter(
                "circle",
                longitude,
                latitude,
                radius
        );

        // Step 3: Construct geographic ranking bias.
        SpatialBias bias = new SpatialBias(
                "proximity",
                longitude,
                latitude
        );

        // Step 4: Construct provider request.
        GeoapifyPlacesRequest request = new GeoapifyPlacesRequest(
                List.of(category.getGeoapifyCategory()),
                filter,
                bias,
                limit,
                0,
                "en"
        );

        // Step 5: Call Geoapify Places API.
        try {

            GeoapifyPlacesResponse response =
                    geoapifyRestClient.post()
                            .uri("/v2/places")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .body(request)
                            .retrieve()
                            .body(GeoapifyPlacesResponse.class);

            if (response == null) {
                throw new IllegalStateException(
                        "Geoapify returned an empty response."
                );
            }

            // Step 6: Convert provider response into our DTO.
            List<PlaceDTO> places = response.features() == null
                    ? List.of()
                    : response.features().stream()
                            .filter(Objects::nonNull)
                            .map(GeoapifyPlacesService::mapToPlaceDTO)
                            .filter(Objects::nonNull)
                            .toList();

            return new PlaceSearchResponseDTO(
                    category.name(),
                    latitude,
                    longitude,
                    radius,
                    places.size(),
                    places
            );

        } catch (RestClientResponseException exception) {

            throw new IllegalStateException(
                    "Geoapify API returned HTTP "
                            + exception.getStatusCode().value()
                            + ". Please check the external API configuration.",
                    exception
            );

        } catch (RestClientException exception) {

            throw new IllegalStateException(
                    "Unable to communicate with Geoapify Places API.",
                    exception
            );
        }
    }

    private void validateCoordinates(
            double latitude,
            double longitude) {

        if (!Double.isFinite(latitude)
                || latitude < -90
                || latitude > 90) {

            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90."
            );
        }

        if (!Double.isFinite(longitude)
                || longitude < -180
                || longitude > 180) {

            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180."
            );
        }
    }

    private static PlaceDTO mapToPlaceDTO(
            GeoapifyPlacesResponse.Feature feature) {

        if (feature.properties() == null) {
            return null;
        }

        GeoapifyPlacesResponse.Properties properties =
                feature.properties();

        return new PlaceDTO(
                properties.name(),
                properties.categories() == null
                        || properties.categories().isEmpty()
                        ? null
                        : properties.categories().get(0),
                properties.formatted(),
                properties.city(),
                properties.state(),
                properties.country(),
                properties.lat(),
                properties.lon(),
                properties.distance(),
                properties.place_id(),
                properties.categories() == null
                        ? List.of()
                        : properties.categories()
        );
    }
}
