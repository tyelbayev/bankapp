package com.example.cashservice.controller;

import com.example.cashservice.dto.CashRequest;
import com.example.cashservice.service.CashService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cash")
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/user/{login}")
    public ResponseEntity<Void> handleCash(@PathVariable String login,
                                           @RequestBody CashRequest request) {
        cashService.processCashOperation(login, request);
        return ResponseEntity.ok().build();
    }
}
