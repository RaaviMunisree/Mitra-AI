package com.mitra.ai.external.duffel.flight.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitra.ai.external.duffel.flight.dto.FlightOfferDTO;
import com.mitra.ai.external.duffel.flight.dto.FlightSearchRequestDTO;
import com.mitra.ai.external.duffel.flight.dto.FlightSearchResponseDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import tools.jackson.databind.JsonNode;

@Service
public class DuffelFlightService {

    private final RestClient duffelRestClient;

    public DuffelFlightService(
            @Qualifier("duffelRestClient") RestClient duffelRestClient) {
        this.duffelRestClient = duffelRestClient;
    }

    public FlightSearchResponseDTO searchFlights(
            FlightSearchRequestDTO request) {

        validateRequest(request);

        List<Map<String, Object>> slices = new ArrayList<>();

        slices.add(Map.of(
                "origin", request.origin().toUpperCase(),
                "destination", request.destination().toUpperCase(),
                "departure_date", request.departureDate().toString()
        ));

        if (request.returnDate() != null) {
            slices.add(Map.of(
                    "origin", request.destination().toUpperCase(),
                    "destination", request.origin().toUpperCase(),
                    "departure_date", request.returnDate().toString()
            ));
        }

        List<Map<String, Object>> passengers = new ArrayList<>();

        for (int i = 0; i < request.adults(); i++) {
            passengers.add(Map.of("type", "adult"));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("slices", slices);
        data.put("passengers", passengers);
        data.put("cabin_class", request.cabinClass());

        if (request.maxConnections() != null) {
            data.put("max_connections", request.maxConnections());
        }

        try {
            JsonNode response = duffelRestClient.post()
                    .uri("/air/offer_requests")
                    .body(Map.of("data", data))
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null || response.path("data").isMissingNode()) {
                throw new IllegalStateException(
                        "Duffel returned an empty flight search response.");
            }

            JsonNode result = response.path("data");
            JsonNode offersNode = result.path("offers");

            List<FlightOfferDTO> offers = new ArrayList<>();

            if (offersNode.isArray()) {
                for (JsonNode offer : offersNode) {
                    offers.add(mapOffer(offer));
                }
            }

            return new FlightSearchResponseDTO(
                    result.path("id").asString(null),
                    result.path("live_mode").asBoolean(false),
                    offers.size(),
                    offers
            );

        } catch (RestClientResponseException exception) {
            throw new IllegalStateException(
                    "Duffel flight search failed with HTTP "
                            + exception.getStatusCode().value()
                            + ". Check the API token and search details.",
                    exception);
        } catch (RestClientException exception) {
            throw new IllegalStateException(
                    "Unable to communicate with Duffel Flights API.",
                    exception);
        }
    }

    private FlightOfferDTO mapOffer(JsonNode offer) {

        List<FlightOfferDTO.FlightSegmentDTO> segments = new ArrayList<>();

        JsonNode slices = offer.path("slices");

        if (slices.isArray()) {
            for (JsonNode slice : slices) {
                JsonNode segmentNodes = slice.path("segments");

                if (!segmentNodes.isArray()) {
                    continue;
                }

                for (JsonNode segment : segmentNodes) {
                    JsonNode operatingCarrier =
                            segment.path("operating_carrier");

                    String airline = operatingCarrier.path("name")
                            .asString(null);

                    segments.add(
                            new FlightOfferDTO.FlightSegmentDTO(
                                    airline,
                                    segment.path("marketing_carrier_flight_number")
                                            .asString(null),
                                    segment.path("origin").path("iata_code")
                                            .asString(null),
                                    segment.path("destination").path("iata_code")
                                            .asString(null),
                                    segment.path("departing_at").asString(null),
                                    segment.path("arriving_at").asString(null)
                            )
                    );
                }
            }
        }

        String owner = offer.path("owner").path("name").asString(null);

        return new FlightOfferDTO(
                offer.path("id").asString(null),
                owner,
                offer.path("total_amount").asString(null),
                offer.path("total_currency").asString(null),
                offer.path("expires_at").asString(null),
                segments
        );
    }

    private void validateRequest(FlightSearchRequestDTO request) {

        if (request.returnDate() != null
                && !request.returnDate().isAfter(request.departureDate())) {
            throw new IllegalArgumentException(
                    "Return date must be after departure date.");
        }

        if (request.origin().equalsIgnoreCase(request.destination())) {
            throw new IllegalArgumentException(
                    "Origin and destination cannot be the same.");
        }
    }
}