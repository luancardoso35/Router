package com.company;

import com.company.Exceptions.FullQueueException;

import java.nio.file.NoSuchFileException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Queue {
    private LinkedList<VirtualPackage> queue;
    private int size;

    public Queue(int size) {
        queue = new LinkedList<>();
        this.size = size;
    }

    public VirtualPackage pop() {
        try {
            return queue.pop();
        } catch(NoSuchElementException nsee) {
            return null;
        }
    }

    public void push(VirtualPackage vp) throws FullQueueException{
        if (queue.size() == size) {
            throw new FullQueueException();
        }
        queue.addLast(vp);
    }

    public VirtualPackage getFirst() {
        return queue.getFirst();
    }

    public int size() {
        return queue.size();
    }
}