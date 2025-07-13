package com.example.frontui.controller;

import com.example.frontui.client.*;
import com.example.frontui.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

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
    public Mono<Rendering> mainPage(Principal principal) {
        if (principal == null) {
            return Mono.just(Rendering.redirectTo("/login").build());
        }

        String login = principal.getName();

        Mono<UserDto> userMono = accountsClient.getUserInfo(login);
        Mono<List<AccountDto>> accountsMono = accountsClient.getAccounts(login);
        Mono<List<CurrencyDto>> currenciesMono = exchangeClient.getCurrencies();
        Mono<List<UserDto>> usersMono = accountsClient.getAllUsers();

        return Mono.zip(userMono, accountsMono, currenciesMono, usersMono)
                .map(tuple -> Rendering.view("main")
                        .modelAttribute("login", login)
                        .modelAttribute("name", tuple.getT1().getName())
                        .modelAttribute("birthdate", tuple.getT1().getBirthdate())
                        .modelAttribute("accounts", tuple.getT2())
                        .modelAttribute("currency", tuple.getT3())
                        .modelAttribute("users", tuple.getT4())
                        .build());
    }




}
