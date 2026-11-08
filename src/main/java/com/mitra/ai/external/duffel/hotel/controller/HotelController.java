package com.mitra.ai.external.duffel.hotel.controller;

import com.mitra.ai.external.duffel.hotel.dto.HotelSearchRequestDTO;
import com.mitra.ai.external.duffel.hotel.dto.HotelSearchResponseDTO;
import com.mitra.ai.external.duffel.hotel.service.DuffelHotelService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/hotels")
public class HotelController {

    private final DuffelHotelService hotelService;

    public HotelController(DuffelHotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/search")
    public ResponseEntity<HotelSearchResponseDTO> searchHotels(
            @Valid @RequestBody HotelSearchRequestDTO request) {

        return ResponseEntity.ok(
                hotelService.searchHotels(request));
    }
}