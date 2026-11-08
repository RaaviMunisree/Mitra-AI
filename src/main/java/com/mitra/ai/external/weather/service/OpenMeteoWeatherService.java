
package com.mitra.ai.external.weather.service;

import java.util.ArrayList;
import java.util.List;

import com.mitra.ai.external.weather.dto.OpenMeteoGeocodingResponse;
import com.mitra.ai.external.weather.dto.OpenMeteoWeatherResponse;
import com.mitra.ai.external.weather.dto.WeatherResponseDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class OpenMeteoWeatherService {

    private final RestClient forecastRestClient;
    private final OpenMeteoGeocodingService geocodingService;

    public OpenMeteoWeatherService(
            @Qualifier("openMeteoForecastRestClient")
            RestClient forecastRestClient,
            OpenMeteoGeocodingService geocodingService) {

        this.forecastRestClient = forecastRestClient;
        this.geocodingService = geocodingService;
    }

    public WeatherResponseDTO getWeatherForCity(String cityName) {

        // Step 1: Resolve the city into geographic coordinates.
        OpenMeteoGeocodingResponse geocodingResponse =
                geocodingService.searchLocations(cityName);

        if (geocodingResponse.results() == null
                || geocodingResponse.results().isEmpty()) {

            throw new IllegalArgumentException(
                    "No location found for city: " + cityName
            );
        }

        OpenMeteoGeocodingResponse.LocationResult location =
                geocodingResponse.results().get(0);

        if (location.latitude() == null
                || location.longitude() == null) {

            throw new IllegalStateException(
                    "Location coordinates are unavailable."
            );
        }

        // Step 2: Retrieve weather forecast using the coordinates.
        OpenMeteoWeatherResponse weatherResponse;

        try {

            weatherResponse = forecastRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/forecast")
                            .queryParam("latitude", location.latitude())
                            .queryParam("longitude", location.longitude())
                            .queryParam("current",
                                    "temperature_2m,"
                                    + "relative_humidity_2m,"
                                    + "apparent_temperature,"
                                    + "precipitation,"
                                    + "weather_code,"
                                    + "cloud_cover,"
                                    + "is_day,"
                                    + "wind_speed_10m,"
                                    + "wind_direction_10m")
                            .queryParam("daily",
                                    "weather_code,"
                                    + "temperature_2m_max,"
                                    + "temperature_2m_min,"
                                    + "precipitation_sum,"
                                    + "precipitation_probability_max,"
                                    + "wind_speed_10m_max")
                            .queryParam("timezone", "auto")
                            .queryParam("forecast_days", 7)
                            .build()
                    )
                    .retrieve()
                    .body(OpenMeteoWeatherResponse.class);

        } catch (RestClientException exception) {

            throw new IllegalStateException(
                    "Unable to retrieve weather information from Open-Meteo.",
                    exception
            );
        }

        if (weatherResponse == null || weatherResponse.current() == null) {

            throw new IllegalStateException(
                    "Open-Meteo returned incomplete weather information."
            );
        }

        // Step 3: Convert provider response into our application DTO.
        return mapToWeatherResponse(location, weatherResponse);
    }

    private WeatherResponseDTO mapToWeatherResponse(
            OpenMeteoGeocodingResponse.LocationResult location,
            OpenMeteoWeatherResponse response) {

        OpenMeteoWeatherResponse.CurrentWeather current =
                response.current();

        WeatherResponseDTO.CurrentWeatherDTO currentWeather =
                new WeatherResponseDTO.CurrentWeatherDTO(
                        current.time(),
                        current.temperature_2m(),
                        current.apparent_temperature(),
                        current.relative_humidity_2m(),
                        current.precipitation(),
                        current.wind_speed_10m(),
                        current.wind_direction_10m(),
                        current.cloud_cover(),
                        current.weather_code(),
                        current.is_day() == null
                                ? null
                                : current.is_day() == 1
                );

        List<WeatherResponseDTO.DailyForecastDTO> dailyForecast =
                mapDailyForecast(response.daily());

        return new WeatherResponseDTO(
                location.name(),
                location.country(),
                response.timezone(),
                currentWeather,
                dailyForecast
        );
    }

    private List<WeatherResponseDTO.DailyForecastDTO> mapDailyForecast(
            OpenMeteoWeatherResponse.DailyWeather daily) {

        if (daily == null || daily.time() == null) {
            return List.of();
        }

        List<WeatherResponseDTO.DailyForecastDTO> forecasts =
                new ArrayList<>();

        for (int i = 0; i < daily.time().size(); i++) {

            forecasts.add(
                    new WeatherResponseDTO.DailyForecastDTO(
                            daily.time().get(i),
                            getValue(daily.temperature_2m_max(), i),
                            getValue(daily.temperature_2m_min(), i),
                            getValue(daily.precipitation_sum(), i),
                            getValue(daily.precipitation_probability_max(), i),
                            getValue(daily.wind_speed_10m_max(), i),
                            getValue(daily.weather_code(), i)
                    )
            );
        }

        return forecasts;
    }

    private <T> T getValue(List<T> values, int index) {

        if (values == null || index >= values.size()) {
            return null;
        }

        return values.get(index);
    }
}
