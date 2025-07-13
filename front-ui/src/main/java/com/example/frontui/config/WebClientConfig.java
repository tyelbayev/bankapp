package com.example.frontui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient gatewayWebClient(
            ReactiveClientRegistrationRepository registrations,
            ServerOAuth2AuthorizedClientRepository authorizedClients,
            @Value("${gateway.base-url}") String baseUrl) {

        var oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                registrations, authorizedClients);
        oauth2.setDefaultOAuth2AuthorizedClient(true);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(oauth2)
                .build();
    }
}

