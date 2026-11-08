
package com.mitra.ai.external.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenMeteoConfig {

    @Bean("openMeteoForecastRestClient")
    public RestClient openMeteoForecastRestClient(
            RestClient.Builder builder,
            @Value("${mitra.external.open-meteo.forecast-base-url}")
            String forecastBaseUrl) {

        return builder.clone()
                .baseUrl(forecastBaseUrl)
                .build();
    }

    @Bean("openMeteoGeocodingRestClient")
    public RestClient openMeteoGeocodingRestClient(
            RestClient.Builder builder,
            @Value("${mitra.external.open-meteo.geocoding-base-url}")
            String geocodingBaseUrl) {

        return builder.clone()
                .baseUrl(geocodingBaseUrl)
                .build();
    }
}
