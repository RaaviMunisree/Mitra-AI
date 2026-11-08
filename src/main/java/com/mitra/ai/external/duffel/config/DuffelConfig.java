package com.mitra.ai.external.duffel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DuffelConfig {

    @Bean("duffelRestClient")
    public RestClient duffelRestClient(
            RestClient.Builder builder,
            @Value("${mitra.external.duffel.base-url}") String baseUrl,
            @Value("${mitra.external.duffel.api-token}") String apiToken) {

        return builder.clone()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiToken)
                .defaultHeader("Duffel-Version", "v2")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}