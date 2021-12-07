package com.company;

import com.company.Exceptions.FullQueueException;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue {
    private LinkedList<VirtualPackage> queue;
    private int size;

    public Queue(int limit) {
        queue = new ConcurrentLinkedQueue<>();
        this.limit = limit;
    }

    /**
     * Método responsável por retirar e retornar o primeiro elemento da Queue
     * @return o primeiro elemento da Queue, ou null se a Queue estiver vazia
     */
    public VirtualPackage pop() {
        try {
            return queue.pop();
        } catch(NoSuchElementException nsee) {
            return null;
        }
    }

    /**
     * Método para inserção de um pacote na Queue
     * @param vp pacote a ser inserido na Queue
     * @throws FullQueueException caso a Queue esteja cheia
     */
    public void push(VirtualPackage vp) throws FullQueueException {
        if (queue.size() == limit) {
            throw new FullQueueException();
        }
        queue.add(vp);
    }

    /**
     * Método para acessar o primeiro elemento da Queue, sem retirá-lo da mesma
     * @return o primeiro elemento da Queue, ou null caso a Queue esteja vazia
     */
    public VirtualPackage getFirst() {
        return queue.peek();
    }

    /**
     * Método para acessar o tamanho da Queue
     * @return o tamanho da Queue
     */
    public int size() {
        return queue.size();
    }
}