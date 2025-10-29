package com.example.exchangeservice.model;

import java.time.Instant;

public record ExchangeEvent(
        long seq,
        String type,
        String payload,
        Instant createdAt
) {}

