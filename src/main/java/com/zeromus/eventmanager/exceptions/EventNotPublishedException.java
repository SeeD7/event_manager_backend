package com.zeromus.eventmanager.exceptions;

public class EventNotPublishedException extends Exception
{
    public EventNotPublishedException() {
        super("L'évènement n'est pas publié");
    }
}
