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
        System.out.println("🟡 Вызван контроллер mainPage");

        if (principal == null) {
            System.out.println("🔒 Principal is null — редирект на /login");
            return Mono.just(Rendering.redirectTo("/login").build());
        }

        String login = principal.getName();

        Mono<UserDto> userMono = accountsClient.getUserInfo(login);
        Mono<List<AccountDto>> accountsMono = accountsClient.getAccounts(login);
        Mono<List<CurrencyDto>> currenciesMono = exchangeClient.getCurrencies();
        Mono<List<UserDto>> usersMono = accountsClient.getAllUsers();

        return Mono.zip(userMono, accountsMono, currenciesMono, usersMono)
                .map(tuple -> {
                    System.out.println("=== Currency list ===");
                    List<CurrencyDto> currencies = tuple.getT3();
                    if (currencies == null || currencies.isEmpty()) {
                        System.out.println("Currency list is empty or null");
                    } else {
                        currencies.forEach(c -> System.out.println("Currency: " + c.getTitle() + " (" + c.getName() + ")"));
                        System.out.println("✔️ Передаём currencies в модель, size = " + currencies.size());
                    }

                    System.out.println("🟢 Перед отдачей Rendering — model:");
                    System.out.println("    login = " + login);
                    System.out.println("    name = " + tuple.getT1().getName());
                    System.out.println("    birthdate = " + tuple.getT1().getBirthdate());
                    System.out.println("    accounts = " + tuple.getT2().size());
                    System.out.println("    currencies = " + (currencies == null ? "null" : currencies.size()));
                    System.out.println("    users = " + tuple.getT4().size());

                    return Rendering.view("main")
                            .modelAttribute("login", login)
                            .modelAttribute("name", tuple.getT1().getName())
                            .modelAttribute("birthdate", tuple.getT1().getBirthdate())
                            .modelAttribute("accounts", tuple.getT2())
                            .modelAttribute("currencies", currencies)
                            .modelAttribute("users", tuple.getT4())
                            .build();
                });

    }





}
