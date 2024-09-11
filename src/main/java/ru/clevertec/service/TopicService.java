package ru.clevertec.service;

import ru.clevertec.model.ConsumerTopic;
import ru.clevertec.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TopicService {

    private final Topic topic;
    private final ConsumerTopic consumer;
    private final CountDownLatch latch;

    public TopicService(String topicName) {
        this.topic = new Topic(topicName);
        List<String> messages = new ArrayList<>();
        Lock lock = new ReentrantLock();
        topic.setMessages(messages);
        topic.setLock(lock);

        this.latch = new CountDownLatch(1);
        this.consumer = new ConsumerTopic(topic, latch);
        consumer.setLastReadIndex(0);
        consumer.setReadMessages(new ArrayList<>());
    }

    public void startConsumer() {
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

    public void publishMessage(String message) {
        topic.publishMessage(message);
    }

    public void awaitLatch() throws InterruptedException {
        latch.await();
    }

    public List<String> getReadMessages() {
        return consumer.getReadMessages();
    }
}
