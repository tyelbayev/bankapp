package com.example.frontui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // на случай если Builder не подкинулся автоконфигом
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient gatewayWebClient(
            WebClient.Builder builder,
            ReactiveClientRegistrationRepository registrations,
            ReactiveOAuth2AuthorizedClientService clientService,
            @Value("${gateway.base-url}") String baseUrl) {

        // токен по client_credentials
        ReactiveOAuth2AuthorizedClientProvider provider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        var manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(registrations, clientService);
        manager.setAuthorizedClientProvider(provider);

        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth.setDefaultClientRegistrationId("front-ui-s2s"); // <-- ID в application.yml

        // временный логгер для проверки, что Bearer реально подставляется
        ExchangeFilterFunction authLog = (req, next) -> {
            String auth = req.headers().getFirst("Authorization");
            System.out.println("➡️ OUT " + req.method() + " " + req.url() + "  Authorization=" + auth);
            return next.exchange(req);
        };

        return builder
                .baseUrl(baseUrl)           // http://localhost:8088 (gateway)
                .filter(oauth)              // подставляет Bearer автоматически
                .filter(authLog)
                .build();
    }
}
