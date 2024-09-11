package ru.clevertec.model;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Topic {

    private final String name;
    private List<String> messages;
    private Lock lock;
    private Condition newMessageCondition;

    public Topic(String name) {
        this.name = name;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
        this.newMessageCondition = lock.newCondition();
    }

    public void publishMessage(String message) {
        lock.lock();
        try {
            messages.add(message);
            newMessageCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public List<String> getMessages() {
        return messages;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getNewMessageCondition() {
        return newMessageCondition;
    }
}