package com.example.exchangegeneratorservice.service;

import com.example.exchangegeneratorservice.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.*;
import java.util.List;
import java.util.Random;

@Service
public class GeneratorService {

    private final WebClient webClient;
    private final List<String> currencies = List.of("RUB", "USD", "CNY");
    private final Random random = new Random();

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
//                        BigDecimal.valueOf(0.5 + (1.5 * random.nextDouble())).setScale(2)
                        BigDecimal.valueOf(0.5 + (1.5 * random.nextDouble())).setScale(2, BigDecimal.ROUND_HALF_UP)
                )).toList();

        webClient.post()
                .uri("/exchange/update")
                .bodyValue(rates)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
