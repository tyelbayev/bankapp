package com.example.exchangegeneratorservice.service;

import com.example.exchangegeneratorservice.dto.KeycloakClientProperties;
import com.example.exchangegeneratorservice.dto.KeycloakProviderProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KeycloakTokenService {

    private final WebClient webClient;
    private final KeycloakClientProperties clientProps;
    private final KeycloakProviderProperties providerProps;

    public KeycloakTokenService(WebClient.Builder builder,
                                KeycloakClientProperties clientProps,
                                KeycloakProviderProperties providerProps) {
        this.webClient = builder.build();
        this.clientProps = clientProps;
        this.providerProps = providerProps;
    }
    public Mono<String> getToken() {
        return webClient.post()
                .uri(providerProps.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientProps.getClientId())
                        .with("client_secret", clientProps.getClientSecret()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("access_token").asText());
    }
}
