package com.example.cashservice.service;

import com.example.cashservice.dto.CashRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CashService {

    private final WebClient webClient;
    private final String baseUrl;

    public CashService(WebClient.Builder builder, @Value("${gateway.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.baseUrl = baseUrl;
    }

    public void processCashOperation(String login, CashRequest request) {
        // 1. Проверка блокировки
        Boolean blocked = webClient.post()
                .uri("/blocker/check")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.TRUE.equals(blocked)) {
            throw new RuntimeException("Operation blocked");
        }

        // 2. Обновление баланса
        webClient.post()
                .uri("/accounts/user/{login}/cash", login)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();

        // 3. Уведомление
        webClient.post()
                .uri("/notifications/user/{login}", login)
                .bodyValue("Operation: " + request.getAction() + " " + request.getValue() + " " + request.getCurrency())
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
