package ru.clevertec.model;

import java.util.List;

public class Broker {

    private final List<Topic> topics;

    public Broker(List<Topic> topics) {
        this.topics = topics;
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public Topic getTopic(String name) {
        return topics.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}