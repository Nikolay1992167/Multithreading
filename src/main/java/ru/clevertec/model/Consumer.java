package ru.clevertec.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer {

    private final Broker broker;
    private final Map<String, Integer> lastReadIndex;

    public Consumer(Broker broker, Map<String, Integer> lastReadIndex) {
        this.broker = broker;
        this.lastReadIndex = lastReadIndex;
    }

    public void consume(String topicName,
                        int maxMessages,
                        CountDownLatch latch,
                        List<String> consumedMessages) throws InterruptedException {

        Topic topic = broker.getTopic(topicName);
        if (topic == null) {
            return;
        }

        Lock lock = topic.getLock();
        Condition newMessage = topic.getNewMessageCondition();

        lock.lock();
        try {
            while (maxMessages > 0) {
                List<String> messages = topic.getMessages();
                int lastIndex = lastReadIndex.getOrDefault(topicName, 0);

                while (lastIndex >= messages.size()) {
                    newMessage.await();
                }

                while (lastIndex < messages.size() && maxMessages > 0) {
                    consumedMessages.add(messages.get(lastIndex));
                    lastIndex++;
                    maxMessages--;
                    latch.countDown();
                }

                lastReadIndex.put(topicName, lastIndex);
            }
        } finally {
            lock.unlock();
        }
    }
}