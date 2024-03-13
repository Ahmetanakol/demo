package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class QueueListener {

    private static final Logger log = LoggerFactory.getLogger(QueueListener.class);
    private final QueueMessageSender messageSender;

    @Autowired
    public QueueListener (QueueMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @JmsListener(destination = "firstqueue", containerFactory = "jmsListenerContainerFactory")
    public void listenFirstQueue(Message queueMessage) {
        messageSender.sendMessage(queueMessage,"secondqueue");
        log.info(String.format("message received: %s", queueMessage.content()));
    }

    @JmsListener(destination = "secondqueue", containerFactory = "jmsListenerContainerFactory")
    public void listenSecondQueue(Message queueMessage) {
        log.info(String.format("message received: %s", queueMessage.content()));
    }
}
