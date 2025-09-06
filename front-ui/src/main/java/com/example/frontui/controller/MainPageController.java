package com.example.frontui.controller;

import com.example.frontui.client.*;
import com.example.frontui.dto.*;
import org.springframework.stereotype.Controller;
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

        Mono<UserDto> userMono = accountsClient.getUserInfo(login)
                .doOnSubscribe(s -> System.out.println("➡️ getUserInfo(" + login + ")"))
                .doOnError(e -> System.out.println("❌ getUserInfo error: " + e.getMessage()))
                .onErrorResume(e -> Mono.just(new UserDto()))
                .switchIfEmpty(Mono.just(new UserDto()));

        Mono<List<AccountDto>> accountsMono = accountsClient.getAccounts(login);
        Mono<List<CurrencyDto>> currenciesMono = exchangeClient.getCurrencies();
        Mono<List<UserDto>> usersMono = accountsClient.getAllUsers();

        return Mono.zip(userMono, accountsMono, currenciesMono, usersMono)
                .map(tuple -> {
                    var user        = tuple.getT1();
                    var accounts    = tuple.getT2();
                    var currencies  = tuple.getT3();
                    var users       = tuple.getT4();

                    return Rendering.view("main")
                            .modelAttribute("login", login)
                            .modelAttribute("name", user.getName())
                            .modelAttribute("birthdate", user.getBirthdate())
                            .modelAttribute("accounts", accounts == null ? java.util.List.of() : accounts)
                            .modelAttribute("currencies", currencies == null ? java.util.List.of() : currencies)
                            .modelAttribute("users", users == null ? java.util.List.of() : users)
                            .build();
                });
    }


}
