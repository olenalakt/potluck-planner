package com.olena.eventservice.publisher;

import com.olena.eventservice.repository.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    @Autowired
    private Source source;

    public void publish(Event event) {
        source.output().send(MessageBuilder.withPayload(event).build());
    }
}
