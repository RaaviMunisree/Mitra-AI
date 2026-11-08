
package com.mitra.ai.external.places.controller;

import com.mitra.ai.external.places.dto.PlaceCategory;
import com.mitra.ai.external.places.dto.PlaceSearchResponseDTO;
import com.mitra.ai.external.places.service.GeoapifyPlacesService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/places")
public class PlacesController {

    private final GeoapifyPlacesService placesService;

    public PlacesController(
            GeoapifyPlacesService placesService) {

        this.placesService = placesService;
    }

    @GetMapping
    public ResponseEntity<PlaceSearchResponseDTO> searchNearbyPlaces(

            @RequestParam PlaceCategory category,

            @RequestParam double latitude,

            @RequestParam double longitude,

            @RequestParam(required = false)
            Integer radiusMeters,

            @RequestParam(required = false)
            Integer limit) {

        PlaceSearchResponseDTO response =
                placesService.searchNearbyPlaces(
                        category,
                        latitude,
                        longitude,
                        radiusMeters,
                        limit
                );

        return ResponseEntity.ok(response);
    }
}
