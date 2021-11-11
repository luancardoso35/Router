package com.company.Exceptions;

public class FullQueueException extends Exception{
    public FullQueueException() {
        super("The queue is full.");
    }
}
