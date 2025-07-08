package com.example.accountsservice.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class OAuthWebClientService {

    private final WebClient webClient;

    public OAuthWebClientService(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("keycloak-client-credentials");

        this.webClient = WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    public String callSecuredService() {
        return webClient.get()
                .uri("http://another-service/api/protected")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
