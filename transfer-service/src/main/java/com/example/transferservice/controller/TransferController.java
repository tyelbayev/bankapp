package com.example.transferservice.controller;

import com.example.transferservice.dto.TransferRequest;
import com.example.transferservice.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/user/{login}")
    public ResponseEntity<Void> transfer(@PathVariable String login,
                                         @RequestBody TransferRequest request) {
        transferService.transfer(login, request);
        return ResponseEntity.ok().build();
    }
}
