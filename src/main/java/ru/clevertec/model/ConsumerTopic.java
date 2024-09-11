package ru.clevertec.model;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConsumerTopic implements Runnable {

    private final Topic topic;
    private int lastReadIndex;
    private final CountDownLatch latch;
    private List<String> readMessages;

    public ConsumerTopic(Topic topic, CountDownLatch latch) {
        this.topic = topic;
        this.latch = latch;
    }

    public void setLastReadIndex(int lastReadIndex) {
        this.lastReadIndex = lastReadIndex;
    }

    public void setReadMessages(List<String> readMessages) {
        this.readMessages = readMessages;
    }

    @Override
    public void run() {
        while (latch.getCount() > 0) {
            topic.getLock().lock();
            try {
                while (lastReadIndex >= topic.getMessages().size()) {
                    topic.getNewMessageCondition().await();
                }
                String message = topic.getMessages().get(lastReadIndex);
                readMessages.add(message);
                lastReadIndex++;
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                topic.getLock().unlock();
            }
        }
    }

    public List<String> getReadMessages() {
        return readMessages;
    }
}