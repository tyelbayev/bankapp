package com.example.accountsservice.kafka;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {
    private final KafkaTemplate<String, NotificationEvent> template;
    @Value("${app.topics.notifications:notifications}") String topic;

    public NotificationPublisher(KafkaTemplate<String, NotificationEvent> template) {
        this.template = template;
    }

    public void send(NotificationEvent evt) {
        template.send(topic, evt.getKey(), evt);
    }
}
