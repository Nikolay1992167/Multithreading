package ru.clevertec.model;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Topic {

    private final String name;
    private final List<String> messages;
    private final Lock lock = new ReentrantLock();
    private final Condition newMessage = lock.newCondition();

    public Topic(String name, List<String> messages) {
        this.name = name;
        this.messages = messages;
    }

    public void publishMessage(String message) {
        lock.lock();
        try {
            messages.add(message);
            newMessage.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getNewMessageCondition() {
        return newMessage;
    }
}