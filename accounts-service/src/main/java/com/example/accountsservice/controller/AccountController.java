package com.example.accountsservice.controller;

import com.example.accountsservice.dto.*;
import com.example.accountsservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public ResponseEntity<List<String>> signup(@RequestBody SignupRequest request) {
        List<String> errors = accountService.validateAndRegister(request);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{login}")
    public UserDto getUser(@PathVariable String login) {
        return accountService.getUserInfo(login);
    }

    @GetMapping("/user/{login}/accounts")
    public List<AccountDto> getUserAccounts(@PathVariable String login) {
        return accountService.getUserAccounts(login);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return accountService.getAllUsers();
    }

    @PostMapping("/user/{login}/change-password")
    public void changePassword(@PathVariable String login, @RequestBody String password) {
        accountService.changePassword(login, password);
    }

    @PostMapping("/user/{login}/update")
    public void updateUser(@PathVariable String login, @RequestBody EditUserRequest request) {
        accountService.updateUser(login, request);
    }

    @PostMapping("/transfer/debit")
    public void debit(@RequestBody TransferOperation op) {
        accountService.changeBalance(op.getLogin(), op.getCurrency(), op.getValue().negate());
    }

    @PostMapping("/transfer/credit")
    public void credit(@RequestBody TransferOperation op) {
        accountService.changeBalance(op.getLogin(), op.getCurrency(), op.getValue());
    }

}
