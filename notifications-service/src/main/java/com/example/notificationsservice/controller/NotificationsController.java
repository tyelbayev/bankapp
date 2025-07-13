package com.example.notificationsservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    private static final Logger log = LoggerFactory.getLogger(NotificationsController.class);

    @PostMapping("/user/{login}")
    public ResponseEntity<Void> notifyUser(@PathVariable String login,
                                           @RequestBody String message) {
        log.info("Notification to [{}]: {}", login, message);
        return ResponseEntity.ok().build();
    }
}
