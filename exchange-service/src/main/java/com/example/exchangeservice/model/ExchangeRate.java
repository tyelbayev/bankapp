package com.example.exchangeservice.model;

import java.math.BigDecimal;

public class ExchangeRate {
    private String name;
    private String title;
    private BigDecimal value;

    public ExchangeRate(String name, String title, BigDecimal value) {
        this.name = name;
        this.title = title;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
