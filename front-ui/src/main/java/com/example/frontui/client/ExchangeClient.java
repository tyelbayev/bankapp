package com.example.frontui.client;

import com.example.frontui.dto.CurrencyDto;
import com.example.frontui.dto.RateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ExchangeClient {

    private final WebClient webClient;

    public ExchangeClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<CurrencyDto>> getCurrencies() {
        return webClient.get()
                .uri("/exchange/currencies")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> System.out.println("📦 Получен JSON: " + json))
                .thenReturn(List.of());
    }

    public List<RateDto> getRates() {
        return webClient.get()
                .uri("/api/rates")
                .retrieve()
                .bodyToFlux(RateDto.class)
                .collectList()
                .block();
    }
}

