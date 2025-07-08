package com.example.accountsservice.controller;

import com.example.accountsservice.service.OAuthWebClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final OAuthWebClientService client;

    public TestController(OAuthWebClientService client) {
        this.client = client;
    }

    @GetMapping("/call")
    public ResponseEntity<String> call() {
        String response = client.callSecuredService();
        return ResponseEntity.ok(response);
    }
}