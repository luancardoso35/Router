package com.company;

import com.company.Exceptions.FullQueueException;

import java.util.LinkedList;

public class Queue {
    private LinkedList<VirtualPackage> queue;
    private int size;

    public Queue(int size) {
        queue = new LinkedList<>();
        this.size = size;
    }

    public VirtualPackage pop(){
        return queue.pop();
    }
    public void push(VirtualPackage vp) throws FullQueueException{
        if (queue.size() == size) {
            throw new FullQueueException();
        }
        queue.addLast(vp);
    }
}