package com.example.exchangeservice.controller;

import com.example.exchangeservice.CurrencyDto;
import com.example.exchangeservice.model.ExchangeRate;
import com.example.exchangeservice.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/currencies")
    public List<CurrencyDto> getCurrencies() {
        return exchangeService.getCurrencyDtos();
    }

    @GetMapping("/rate/{currency}")
    public BigDecimal getRate(@PathVariable String currency) {
        return exchangeService.getRate(currency);
    }

    @PostMapping("/update")
    public void updateRates(@RequestBody List<ExchangeRate> rates) {
        System.out.println("Received rates: " + rates);
        exchangeService.updateRates(rates);
    }

    @GetMapping("/rates")
    public List<ExchangeRate> getAllRates() {
        return exchangeService.getAllRates();
    }

}
