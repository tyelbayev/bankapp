package com.example.blockerservice.service;

import com.example.blockerservice.dto.OperationRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class BlockerService {

    private final Random random = new Random();

    public boolean isBlocked(OperationRequest request) {
        if (request.getValue().doubleValue() > 10000) return true;
        return random.nextInt(10) == 0;
    }
}
