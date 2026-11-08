
package com.mitra.ai.external.places.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GeoapifyConfig {

    @Bean("geoapifyRestClient")
    public RestClient geoapifyRestClient(
            RestClient.Builder builder,
            @Value("${mitra.external.geoapify.base-url}")
            String baseUrl,
            @Value("${mitra.external.geoapify.api-key}")
            String apiKey) {

        return builder.clone()
                .baseUrl(baseUrl)
                .defaultHeader("x-api-key", apiKey)
                .build();
    }
}
