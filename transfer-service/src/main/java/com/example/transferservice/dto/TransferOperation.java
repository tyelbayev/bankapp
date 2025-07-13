package com.example.transferservice.dto;

import java.math.BigDecimal;

public class TransferOperation {
    private String login;
    private String currency;
    private BigDecimal value;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public TransferOperation(String login, String currency, BigDecimal value) {
        this.login = login;
        this.currency = currency;
        this.value = value;
    }

}
