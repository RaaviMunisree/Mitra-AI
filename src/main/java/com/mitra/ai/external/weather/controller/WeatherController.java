
package com.mitra.ai.external.weather.controller;

import com.mitra.ai.external.weather.dto.WeatherResponseDTO;
import com.mitra.ai.external.weather.service.OpenMeteoWeatherService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/weather")
public class WeatherController {

    private final OpenMeteoWeatherService weatherService;

    public WeatherController(
            OpenMeteoWeatherService weatherService) {

        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponseDTO> getWeather(
            @RequestParam String city) {

        WeatherResponseDTO response =
                weatherService.getWeatherForCity(city);

        return ResponseEntity.ok(response);
    }
}
