package com.company.Exceptions;

public class FullQueueException extends Exception{
    /**
     * Construtor da classe FullQueueException, usada como exceção em casos de fila cheia
     */
    public FullQueueException() {
        super("The queue is full.");
    }
}
