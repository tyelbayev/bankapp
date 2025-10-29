package com.example.transferservice.kafka;


import com.example.transferservice.dto.NotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {
    private final KafkaTemplate<String, Object> template;
    @Value("${app.topics.notifications:notifications}") String topic;

    public NotificationPublisher(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void send(NotificationEvent evt) {
        template.send(topic, evt.getKey(), evt);
    }
}
