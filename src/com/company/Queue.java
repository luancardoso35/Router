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

    public VirtualPackage pop() {
        try {
            return queue.pop();
        } catch(NoSuchElementException nsee) {
            return null;
        }
    }

    public void push(VirtualPackage vp) throws FullQueueException {
        if (queue.size() == limit) {
            throw new FullQueueException();
        }
        queue.add(vp);
    }

    public VirtualPackage getFirst() {
        return queue.peek();
    }

    public int size() {
        return queue.size();
    }
}