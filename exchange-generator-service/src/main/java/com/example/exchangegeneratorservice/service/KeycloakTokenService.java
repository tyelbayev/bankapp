package com.example.exchangegeneratorservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KeycloakTokenService {

    private final WebClient webClient;
    private final String tokenUrl = "http://localhost:8089/realms/bankapp/protocol/openid-connect/token";
    private final String clientId = "exchange-generator";
    private final String clientSecret = "secret";

    public KeycloakTokenService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Mono<String> getToken() {
        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("access_token").asText());
    }
}
