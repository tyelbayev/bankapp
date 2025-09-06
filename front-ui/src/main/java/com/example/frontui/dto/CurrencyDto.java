package com.example.frontui.dto;

public class CurrencyDto {
    private String title;
    private String name;

    public CurrencyDto(String доллар, String usd) {
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CurrencyDto() {}
}
