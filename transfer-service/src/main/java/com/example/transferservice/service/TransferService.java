package com.example.transferservice.service;

import com.example.transferservice.dto.TransferOperation;
import com.example.transferservice.dto.TransferRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final WebClient webClient;

    public TransferService(WebClient.Builder builder, @Value("${gateway.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public void transfer(String login, TransferRequest request) {
        Boolean blocked = webClient.post()
                .uri("/blocker/check")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.TRUE.equals(blocked)) {
            throw new RuntimeException("Operation blocked");
        }

        BigDecimal amount = request.getValue();
        if (!request.getFromCurrency().equalsIgnoreCase(request.getToCurrency())) {
            BigDecimal rateFrom = getExchangeRate(request.getFromCurrency());
            BigDecimal rateTo = getExchangeRate(request.getToCurrency());

            BigDecimal rubles = amount.multiply(rateFrom); // из валюты в рубли
            amount = rubles.divide(rateTo, 2, BigDecimal.ROUND_HALF_UP);
        }

        TransferOperation debit = new TransferOperation(login, request.getFromCurrency(), request.getValue().negate());
        webClient.post()
                .uri("/accounts/transfer/debit")
                .bodyValue(debit)
                .retrieve()
                .toBodilessEntity()
                .block();

        String recipient = request.getToLogin() != null ? request.getToLogin() : login;
        TransferOperation credit = new TransferOperation(recipient, request.getToCurrency(), amount);
        webClient.post()
                .uri("/accounts/transfer/credit")
                .bodyValue(credit)
                .retrieve()
                .toBodilessEntity()
                .block();

        webClient.post()
                .uri("/notifications/user/{login}", login)
                .bodyValue("Transferred " + request.getValue() + " " + request.getFromCurrency() +
                        " to " + recipient + " (" + amount + " " + request.getToCurrency() + ")")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private BigDecimal getExchangeRate(String currency) {
        return webClient.get()
                .uri("/exchange/rate/{currency}", currency)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();
    }


}
