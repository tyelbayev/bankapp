package com.example.frontui.dto;

import java.time.LocalDate;
import java.util.List;

public class EditUserRequest {
    private String name;
    private LocalDate birthdate;
    private List<String> currencies;

    // Конструкторы, геттеры/сеттеры

    public EditUserRequest(String name, LocalDate birthdate, List<String> currencies) {
        this.name = name;
        this.birthdate = birthdate;
        this.currencies = currencies;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public List<String> getCurrencies() {
        return currencies;
    }
}
