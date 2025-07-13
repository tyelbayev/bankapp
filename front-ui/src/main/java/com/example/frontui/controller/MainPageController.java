package com.example.frontui.controller;

import com.example.frontui.client.*;
import com.example.frontui.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping
public class MainPageController {

    private final AccountsClient accountsClient;
    private final ExchangeClient exchangeClient;

    public MainPageController(AccountsClient accountsClient, ExchangeClient exchangeClient) {
        this.accountsClient = accountsClient;
        this.exchangeClient = exchangeClient;
    }

    @GetMapping({"/", "/main"})
    public String mainPage(Principal principal, Model model) {
        String login = principal.getName();

        UserDto user = accountsClient.getUserInfo(login);
        List<AccountDto> accounts = accountsClient.getAccounts(login);
        List<CurrencyDto> currencies = exchangeClient.getCurrencies();
        List<UserDto> users = accountsClient.getAllUsers();

        model.addAttribute("login", login);
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        model.addAttribute("accounts", accounts);
        model.addAttribute("currency", currencies);
        model.addAttribute("users", users);

        return "main";
    }
}
