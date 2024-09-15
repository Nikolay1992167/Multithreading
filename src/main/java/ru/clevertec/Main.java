package ru.clevertec;

import ru.clevertec.model.Broker;
import ru.clevertec.model.Producer;
import ru.clevertec.model.Topic;
import ru.clevertec.service.BrokerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        BrokerService brokerService = new BrokerService();
        Broker broker = brokerService.getBroker();
        Topic topic = new Topic("First topic", new ArrayList<>());
        broker.addTopic(topic);

        Producer producer = new Producer(broker);

        CountDownLatch latch = new CountDownLatch(5);
        List<String> consumedMessages = new ArrayList<>();

        brokerService.startConsuming("First topic", 5, latch, consumedMessages);

        producer.produce("First topic", "Message 1");
        producer.produce("First topic", "Message 2");
        producer.produce("First topic", "Message 3");
        producer.produce("First topic", "Message 4");
        producer.produce("First topic", "Message 5");

        latch.await();
        System.out.println("Consumed messages: " + consumedMessages);
    }
}