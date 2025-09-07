package com.example.frontui.client;

import com.example.frontui.dto.AccountDto;
import com.example.frontui.dto.EditUserRequest;
import com.example.frontui.dto.SignupRequest;
import com.example.frontui.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class AccountsClient {

    private final WebClient webClient;

    public AccountsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<String> validateAndRegister(SignupRequest request) {
        return webClient.post()
                .uri("/accounts/signup")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
    }
    public Mono<UserDto> getUserInfo(String login) {
        return webClient.get()
                .uri("/accounts/user/{login}", login)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<List<AccountDto>> getAccounts(String login) {
        return webClient.get()
                .uri("/accounts/user/{login}/accounts", login) // если baseUrl=gateway; иначе убери "/accounts"
                .retrieve()
                .bodyToFlux(AccountDto.class)   // читаем массив объектов
                .collectList()
                .doOnSubscribe(s -> System.out.println("➡️ getAccounts(" + login + ")"))
                .doOnNext(list -> System.out.println("✅ ACC size=" + (list != null ? list.size() : -1)))
                // 4xx/5xx сюда
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.out.println("💥 getAccounts HTTP " + ex.getRawStatusCode());
                    System.out.println("↳ body: " + ex.getResponseBodyAsString());
                    return Mono.just(List.of());
                })
                // любые прочие ошибки (сетевые и т.д.)
                .onErrorResume(ex -> {
                    System.out.println("💥 getAccounts failed: " + ex);
                    return Mono.just(List.of());
                });
    }




    public Mono<List<UserDto>> getAllUsers() {
        return webClient.get()
                .uri("/accounts/users")
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList();
    }

    public void changePassword(String login, String newPassword) {
        webClient.post()
                .uri("/accounts/user/{login}/change-password", login)
                .bodyValue(newPassword)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void updateUser(String login, String name, String birthdate, List<String> currencies) {
        EditUserRequest request = new EditUserRequest(
                name,
                LocalDate.parse(birthdate),
                currencies
        );

        webClient.post()
                .uri("/accounts/user/{login}/update", login)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
