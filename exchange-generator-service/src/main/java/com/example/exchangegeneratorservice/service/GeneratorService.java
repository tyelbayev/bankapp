package com.example.exchangegeneratorservice.service;

import com.example.exchangegeneratorservice.model.ExchangeRate;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class GeneratorService {

    private final WebClient webClient;
    private final List<String> currencies = List.of("RUB", "USD", "CNY");
    private final Random random = new Random();
    @Autowired
    private KeycloakTokenService tokenService;

    public GeneratorService(WebClient.Builder builder,
                            @Value("${gateway.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Scheduled(fixedRate = 1000)
    public void sendRates() {
        List<ExchangeRate> rates = currencies.stream()
                .filter(c -> !c.equals("RUB"))
                .map(c -> new ExchangeRate(
                        c,
                        switch (c) {
                            case "USD" -> "Доллар";
                            case "CNY" -> "Юань";
                            default -> c;
                        },
                        BigDecimal.valueOf(0.5 + (1.5 * random.nextDouble()))
                                .setScale(2, BigDecimal.ROUND_HALF_UP)
                )).toList();

        tokenService.getToken()
                .flatMap(token -> webClient.post()
                        .uri("exchange/update")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .bodyValue(rates)
                        .retrieve()
                        .toBodilessEntity()
                )
                .doOnError(error -> System.err.println("Failed to send rates: " + error.getMessage()))
                .subscribe();
    }

}
