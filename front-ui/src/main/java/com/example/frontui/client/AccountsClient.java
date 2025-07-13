package com.example.frontui.client;

import com.example.frontui.dto.AccountDto;
import com.example.frontui.dto.EditUserRequest;
import com.example.frontui.dto.SignupRequest;
import com.example.frontui.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Component
public class AccountsClient {

    private final WebClient webClient;

    public AccountsClient(WebClient.Builder webClientBuilder,
                          @Value("${gateway.base-url}") String gatewayBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(gatewayBaseUrl).build();
    }

    public List<String> validateAndRegister(SignupRequest request) {
        return webClient.post()
                .uri("/accounts/signup")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class) // допустим, backend возвращает список ошибок или пусто
                .collectList()
                .block();
    }

    public UserDto getUserInfo(String login) {
        return webClient.get()
                .uri("/accounts/user/{login}", login)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public List<AccountDto> getAccounts(String login) {
        return webClient.get()
                .uri("/accounts/user/{login}/accounts", login)
                .retrieve()
                .bodyToFlux(AccountDto.class)
                .collectList()
                .block();
    }

    public List<UserDto> getAllUsers() {
        return webClient.get()
                .uri("/accounts/users")
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList()
                .block();
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
