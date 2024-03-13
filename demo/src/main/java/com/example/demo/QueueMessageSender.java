package com.example.demo;

import org.apache.qpid.jms.message.JmsTextMessage;
import org.apache.qpid.jms.provider.amqp.message.AmqpJmsMessageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;

@Service
public class QueueMessageSender {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public QueueMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(Message queueMessage, String queueName) {
        jmsTemplate.send(
                queueName,
                session -> {
                    var message = jmsTemplate.getMessageConverter().toMessage(queueMessage, session);
                    AmqpJmsMessageFacade facade = (AmqpJmsMessageFacade) ((JmsTextMessage) message).getFacade();

                    facade.setTracingAnnotation(
                            "x-opt-scheduled-enqueue-time",
                            Date.from(OffsetDateTime.now()
                                    .plusSeconds(1)
                                    .toInstant()));
                    return message;
                });
    }
}
