package me.twocoffee.service.impl;

import me.twocoffee.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void send(ApplicationEvent applicationEvent) {
	applicationContext.publishEvent(applicationEvent);
    }

}
