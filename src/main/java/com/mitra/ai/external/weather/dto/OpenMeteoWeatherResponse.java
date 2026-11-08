
package com.mitra.ai.external.weather.dto;

import java.util.List;

public record OpenMeteoWeatherResponse(

        double latitude,
        double longitude,
        String timezone,
        String timezone_abbreviation,

        CurrentWeather current,
        DailyWeather daily

) {

    public record CurrentWeather(

            String time,
            Integer interval,
            Double temperature_2m,
            Double relative_humidity_2m,
            Double apparent_temperature,
            Integer is_day,
            Double precipitation,
            Double rain,
            Integer weather_code,
            Double cloud_cover,
            Double wind_speed_10m,
            Double wind_direction_10m

    ) {
    }

    public record DailyWeather(

            List<String> time,
            List<Integer> weather_code,
            List<Double> temperature_2m_max,
            List<Double> temperature_2m_min,
            List<Double> precipitation_sum,
            List<Integer> precipitation_probability_max,
            List<Double> wind_speed_10m_max

    ) {
    }
}
