package com.example.accountsservice.service;

import com.example.accountsservice.dto.*;
import com.example.accountsservice.model.AccountEntity;
import com.example.accountsservice.model.UserEntity;
import com.example.accountsservice.repository.AccountRepository;
import com.example.accountsservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    public List<String> validateAndRegister(SignupRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return List.of("Passwords do not match");
        }
        if (userRepository.existsById(request.getLogin())) {
            return List.of("User with this login already exists");
        }

        UserEntity user = new UserEntity();
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setBirthdate(request.getBirthdate());

        userRepository.save(user);
        return List.of();
    }

    public UserDto getUserInfo(String login) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto dto = new UserDto();
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setBirthdate(user.getBirthdate());
        return dto;
    }

    public List<AccountDto> getUserAccounts(String login) {
        UserEntity user = userRepository.findById(login).orElseThrow();
        return accountRepository.findByUser(user).stream()
                .map(entity -> {
                    AccountDto dto = new AccountDto();
                    dto.setCurrency(entity.getCurrency());
                    dto.setValue(entity.getValue());
                    dto.setExists(true);
                    return dto;
                })
                .toList();
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setLogin(user.getLogin());
                    dto.setName(user.getName());
                    dto.setBirthdate(user.getBirthdate());
                    return dto;
                })
                .toList();
    }


    public void changePassword(String login, String password) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(password);
        userRepository.save(user);
    }


    public void updateUser(String login, EditUserRequest request) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setBirthdate(request.getBirthdate());

        accountRepository.deleteAll(accountRepository.findByUser(user));

        List<AccountEntity> newAccounts = request.getCurrencies().stream()
                .map(currency -> {
                    AccountEntity acc = new AccountEntity();
                    acc.setCurrency(currency);
                    acc.setValue(0.0); // по умолчанию
                    acc.setUser(user);
                    return acc;
                })
                .toList();

        user.setAccounts(newAccounts);
        userRepository.save(user);
    }

    public void changeBalance(String login, String currency, BigDecimal delta) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AccountEntity> accounts = accountRepository.findByUser(user);
        AccountEntity account = accounts.stream()
                .filter(a -> a.getCurrency().equalsIgnoreCase(currency))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account in currency not found"));

        BigDecimal newValue = BigDecimal.valueOf(account.getValue()).add(delta);
        if (newValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setValue(newValue.doubleValue());
        accountRepository.save(account);
    }

}
