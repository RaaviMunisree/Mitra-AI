
package com.mitra.ai.external.weather.dto;

import java.util.List;

public record WeatherResponseDTO(

        String city,
        String country,
        String timezone,

        CurrentWeatherDTO currentWeather,

        List<DailyForecastDTO> dailyForecast

) {

    public record CurrentWeatherDTO(

            String time,
            Double temperature,
            Double feelsLike,
            Double humidity,
            Double precipitation,
            Double windSpeed,
            Double windDirection,
            Double cloudCover,
            Integer weatherCode,
            Boolean daytime

    ) {
    }

    public record DailyForecastDTO(

            String date,
            Double maximumTemperature,
            Double minimumTemperature,
            Double precipitation,
            Integer precipitationProbability,
            Double maximumWindSpeed,
            Integer weatherCode

    ) {
    }
}
