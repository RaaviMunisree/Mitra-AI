package com.mitra.ai.external.duffel.hotel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mitra.ai.external.duffel.hotel.dto.HotelDTO;
import com.mitra.ai.external.duffel.hotel.dto.HotelSearchRequestDTO;
import com.mitra.ai.external.duffel.hotel.dto.HotelSearchResponseDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import tools.jackson.databind.JsonNode;

@Service
public class DuffelHotelService {

    private final RestClient duffelRestClient;

    public DuffelHotelService(
            @Qualifier("duffelRestClient") RestClient duffelRestClient) {
        this.duffelRestClient = duffelRestClient;
    }

    public HotelSearchResponseDTO searchHotels(
            HotelSearchRequestDTO request) {

        validateRequest(request);

        List<Map<String, Object>> guests = new ArrayList<>();

        for (int i = 0; i < request.adults(); i++) {
            guests.add(Map.of("type", "adult"));
        }

        Map<String, Object> coordinates = Map.of(
                "latitude", request.latitude(),
                "longitude", request.longitude()
        );

        Map<String, Object> location = Map.of(
                "radius", request.radiusKm(),
                "geographic_coordinates", coordinates
        );

        Map<String, Object> data = Map.of(
                "check_in_date", request.checkInDate().toString(),
                "check_out_date", request.checkOutDate().toString(),
                "rooms", request.rooms(),
                "guests", guests,
                "location", location,
                "free_cancellation_only",
                        Boolean.TRUE.equals(request.freeCancellationOnly())
        );

        try {
            JsonNode response = duffelRestClient.post()
                    .uri("/stays/search")
                    .body(Map.of("data", data))
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null || response.path("data").isMissingNode()) {
                throw new IllegalStateException(
                        "Duffel returned an empty hotel search response.");
            }

            JsonNode results = response.path("data").path("results");

            List<HotelDTO> hotels = new ArrayList<>();

            if (results.isArray()) {
                for (JsonNode result : results) {
                    HotelDTO hotel = mapHotel(result);
                    if (hotel != null) {
                        hotels.add(hotel);
                    }
                }
            }

            return new HotelSearchResponseDTO(
                    hotels.size(), hotels);

        } catch (RestClientResponseException exception) {
            throw new IllegalStateException(
                    "Duffel hotel search failed with HTTP "
                            + exception.getStatusCode().value()
                            + ". Check Stays API access and search details.",
                    exception);
        } catch (RestClientException exception) {
            throw new IllegalStateException(
                    "Unable to communicate with Duffel Stays API.",
                    exception);
        }
    }

    private HotelDTO mapHotel(JsonNode result) {

        JsonNode accommodation = result.path("accommodation");

        if (accommodation.isMissingNode()) {
            return null;
        }

        JsonNode location = accommodation.path("location");
        JsonNode address = location.path("address");
        JsonNode coordinates = location.path("geographic_coordinates");

        return new HotelDTO(
                accommodation.path("id").asString(null),
                accommodation.path("name").asString(null),
                accommodation.path("description").asString(null),
                address.path("line_one").asString(null),
                address.path("city_name").asString(null),
                address.path("country_code").asString(null),
                coordinates.path("latitude").isNumber()
                        ? coordinates.path("latitude").doubleValue() : null,
                coordinates.path("longitude").isNumber()
                        ? coordinates.path("longitude").doubleValue() : null,
                accommodation.path("rating").isNumber()
                        ? accommodation.path("rating").intValue() : null,
                accommodation.path("review_score").isNumber()
                        ? accommodation.path("review_score").doubleValue() : null,
                accommodation.path("review_count").isNumber()
                        ? accommodation.path("review_count").intValue() : null,
                result.path("cheapest_rate_total_amount").asString(null),
                result.path("cheapest_rate_currency").asString(null),
                result.path("check_in_date").asString(null),
                result.path("check_out_date").asString(null),
                result.path("expires_at").asString(null)
        );
    }

    private void validateRequest(HotelSearchRequestDTO request) {

        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date.");
        }

        if (!Double.isFinite(request.latitude())
                || request.latitude() < -90
                || request.latitude() > 90) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90.");
        }

        if (!Double.isFinite(request.longitude())
                || request.longitude() < -180
                || request.longitude() > 180) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180.");
        }
    }
}