package com.example.frontui.client;

import com.example.frontui.dto.CurrencyDto;
import com.example.frontui.dto.RateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ExchangeClient {

    private final WebClient webClient;
    private final String gatewayBaseUrl;

    public ExchangeClient(WebClient.Builder webClientBuilder,
                          @Value("${gateway.base-url}") String gatewayBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(gatewayBaseUrl).build();
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public List<CurrencyDto> getCurrencies() {
        return webClient.get()
                .uri("/exchange/currencies")
                .retrieve()
                .bodyToFlux(CurrencyDto.class)
                .collectList()
                .block();
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

