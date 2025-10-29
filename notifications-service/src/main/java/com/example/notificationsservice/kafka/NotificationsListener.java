package com.example.notificationsservice.kafka;

import com.example.notificationsservice.dto.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class NotificationsListener {

    @KafkaListener(topics = "${app.topics.notifications:notifications}", id = "notifications-listener")
    public void onMessage(NotificationEvent payload,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key,
                          Acknowledgment ack) {
        try {
            process(payload);
            ack.acknowledge();
        } catch (Exception e) {
            throw e;
        }
    }

    private void process(NotificationEvent evt) {
    }
}
