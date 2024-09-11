package ru.clevertec;

import ru.clevertec.service.TopicService;

public class TopicManagementSystem {

    public static void main(String[] args) {
        TopicService topicService = new TopicService("MyTopic");

        topicService.startConsumer();
        topicService.publishMessage("The message has been sent!");

        try {
            topicService.awaitLatch();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error while waiting for lock", e);
        }

        System.out.println("Read Messages: " + topicService.getReadMessages());
    }
}