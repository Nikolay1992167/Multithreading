package ru.clevertec.service;

import ru.clevertec.model.Broker;
import ru.clevertec.model.Consumer;
import ru.clevertec.model.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class BrokerService {

    private final Broker broker;
    private final Map<String, Integer> lastReadIndex;

    public BrokerService() {
        List<Topic> topics = new ArrayList<>();
        this.broker = new Broker(topics);
        this.lastReadIndex = new HashMap<>();
    }


    public Broker getBroker() {
        return broker;
    }

    public void startConsuming(String topicName,
                               int maxMessages,
                               CountDownLatch latch,
                               List<String> consumedMessages) {

        Consumer consumer = new Consumer(broker, lastReadIndex);
        CompletableFuture.runAsync(() -> {
            try {
                consumer.consume(topicName, maxMessages, latch, consumedMessages);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}