package com.example.exchangeservice.kafka;

import com.example.exchangeservice.model.ExchangeEvent;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExchangeListener implements ConsumerSeekAware {

    @KafkaListener(topics = "${app.topics.exchange:exchange-events}", id = "exchange-listener")
    public void onMessage(ExchangeEvent payload) {
        handle(payload);
    }

    private void handle(ExchangeEvent e) {/* ... */}

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.keySet().forEach(tp -> callback.seekToEnd(tp.topic(), tp.partition()));
    }
}
