package com.company;

import com.company.Exceptions.FullQueueException;
import java.util.ArrayList;

public class OutputPort extends Thread{
    private String port_id;
    private Queue virtual_package_queue;
    private double package_forward_probability;
    private double package_transmission_delay;
    private double retransmission_probability;
    private double package_retransmission_probability;
    private ArrayList<String> relayed_packages;
    private ArrayList<String> sent_packages;

    public OutputPort(String port_id, int size, double package_forward_probability,
                      double package_transmission_delay, double retransmission_probability) {
        this.port_id = port_id;
        this.package_forward_probability = package_forward_probability;
        this.package_transmission_delay = package_transmission_delay;
        this.package_retransmission_probability = retransmission_probability;
        this.retransmission_probability = retransmission_probability;
        this.virtual_package_queue = new Queue(size);
    }

    @Override
    public void run() {

    }

    private boolean send (VirtualPackage virtual_package) {
        double random_number = Math.random()*100d;
        double success_probability = 100d - package_retransmission_probability;
        if (random_number < success_probability) {
            virtual_package_queue.pop();
            this.package_retransmission_probability = this.retransmission_probability;
            return true;
        } else {
            relayed_packages.add(virtual_package.toString());
            this.package_retransmission_probability /= 2;
            virtual_package.setLocalDateTime(java.time.LocalDateTime.now());
            return false;
        }
    }

    public boolean insert_package (VirtualPackage virtual_package) {
        try {
            this.virtual_package_queue.push(virtual_package);
            return true;
        } catch(FullQueueException fqe) {
            return false;
        }
    }

    public double getPackage_forward_probability() {
        return package_forward_probability;
    }
}
