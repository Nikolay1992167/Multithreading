package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.model.Broker;
import ru.clevertec.model.Producer;
import ru.clevertec.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrokerServiceTest {
    private BrokerService brokerService;
    private Broker broker;

    @BeforeEach
    void setUp() {
        brokerService = new BrokerService();
        broker = brokerService.getBroker();
    }

    @Test
    void testBrokerInitialization() {
        assertNotNull(brokerService.getBroker());
        assertNotNull(brokerService.getLastReadIndex());
    }

    @Test
    void testAddAndRetrieveTopic() {
        // given
        Topic topic = new Topic("test_topic", new ArrayList<>());
        broker.addTopic(topic);

        // when
        Topic retrievedTopic = broker.getTopic("test_topic");

        // then
        assertNotNull(retrievedTopic);
        assertEquals("test_topic", retrievedTopic.getName());
    }

    @Test
    void testPublishAndConsumeMessages() throws InterruptedException {
        // given
        Topic topic = new Topic("test_topic", new ArrayList<>());
        broker.addTopic(topic);

        Producer producer = new Producer(broker);
        producer.produce("test_topic", "Message 1");
        producer.produce("test_topic", "Message 2");

        CountDownLatch latch = new CountDownLatch(2);
        List<String> consumedMessages = new ArrayList<>();

        // when
        brokerService.startConsuming("test_topic", 2, latch, consumedMessages);
        latch.await();

        // then
        assertEquals(2, consumedMessages.size());
        assertTrue(consumedMessages.contains("Message 1"));
        assertTrue(consumedMessages.contains("Message 2"));
    }
}