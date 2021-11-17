package com.company;

public class CommutingElement extends Thread{
    private double switch_delay;

    public CommutingElement(double switch_delay, ThreadGroup input_threads, ThreadGroup output_threads) {
        this.switch_delay = switch_delay;
    }

    @Override
    public void run() {
    }


}
