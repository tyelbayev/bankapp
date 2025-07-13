package com.example.exchangeservice.service;

import com.example.exchangeservice.CurrencyDto;
import com.example.exchangeservice.model.ExchangeRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangeService {

    private final Map<String, ExchangeRate> rates = new ConcurrentHashMap<>();

    public void updateRates(List<ExchangeRate> newRates) {
        for (ExchangeRate rate : newRates) {
            rates.put(rate.getName().toUpperCase(), rate);
        }
    }

    public BigDecimal getRate(String currency) {
        if (currency.equalsIgnoreCase("RUB")) {
            return BigDecimal.ONE;
        }
        ExchangeRate rate = rates.get(currency.toUpperCase());
        if (rate == null) {
            throw new RuntimeException("Currency not found");
        }
        return rate.getValue();
    }

    public List<CurrencyDto> getCurrencyDtos() {
        return rates.entrySet().stream()
                .map(e -> new CurrencyDto(
                        switch (e.getKey()) {
                            case "USD" -> "Доллар";
                            case "CNY" -> "Юань";
                            default    -> "Рубль";
                        },
                        e.getKey()))
                .toList();
    }

    public List<ExchangeRate> getAllRates() {
        return new ArrayList<>(rates.values());
    }

}
