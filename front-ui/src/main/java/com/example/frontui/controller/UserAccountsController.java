package com.example.frontui.controller;
import com.example.frontui.client.AccountsClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class UserAccountsController {

    private final AccountsClient accountsClient;

    public UserAccountsController(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    @PostMapping("/user/{login}/editUserAccounts")
    public Mono<Rendering> editUserAccounts(
            @PathVariable String login,
            @RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam(name = "currencies", required = false) java.util.List<String> currencies
    ) {
        var list = currencies != null ? currencies : java.util.List.<String>of();

        return Mono.fromRunnable(() -> accountsClient.updateUser(login, name, birthdate, list))
                .then(Mono.just(Rendering.redirectTo("/main?updated=1").build()));
    }
}
