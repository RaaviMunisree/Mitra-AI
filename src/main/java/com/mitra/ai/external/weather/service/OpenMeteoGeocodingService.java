
package com.mitra.ai.external.weather.service;

import com.mitra.ai.external.weather.dto.OpenMeteoGeocodingResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class OpenMeteoGeocodingService {

    private final RestClient geocodingRestClient;

    public OpenMeteoGeocodingService(
            @Qualifier("openMeteoGeocodingRestClient")
            RestClient geocodingRestClient) {

        this.geocodingRestClient = geocodingRestClient;
    }

    public OpenMeteoGeocodingResponse searchLocations(String cityName) {

        // Step 1: Validate the city name.
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException(
                    "City name cannot be empty."
            );
        }

        String normalizedCityName = cityName.trim();

        if (normalizedCityName.length() < 2) {
            throw new IllegalArgumentException(
                    "City name must contain at least 2 characters."
            );
        }

        // Step 2: Call the Open-Meteo Geocoding API.
        try {

            OpenMeteoGeocodingResponse response =
                    geocodingRestClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/v1/search")
                                    .queryParam("name", normalizedCityName)
                                    .queryParam("count", 5)
                                    .queryParam("language", "en")
                                    .queryParam("format", "json")
                                    .build()
                            )
                            .retrieve()
                            .body(OpenMeteoGeocodingResponse.class);

            // Step 3: Validate the API response.
            if (response == null) {
                throw new IllegalStateException(
                        "Open-Meteo returned an empty response."
                );
            }

            return response;

        } catch (RestClientException exception) {

            throw new IllegalStateException(
                    "Unable to retrieve location information from Open-Meteo.",
                    exception
            );
        }
    }
}
