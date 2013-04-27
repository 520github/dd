package me.twocoffee.rest.entity;

import java.util.Map;

public class ClientEvent {
    public static class Event {
	private EventAction action;
	private String target;
	private Map<String, Object> argument;

	public Map<String, Object> getArgument() {
	    return argument;
	}

	public void setArgument(Map<String, Object> argument) {
	    this.argument = argument;
	}

	public EventAction getAction() {
	    return action;
	}

	public String getTarget() {
	    return target;
	}

	public void setAction(EventAction action) {
	    this.action = action;
	}

	public void setTarget(String target) {
	    this.target = target;
	}
    }

    public static enum EventAction {
	visit, // 查看
	Complete,
	Change,
	ThirdpartySharing
    }

    private Event event;

    public Event getEvent() {
	return event;
    }

    public void setEvent(Event event) {
	this.event = event;
    }
}
