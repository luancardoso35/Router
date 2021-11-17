package com.company;

import com.company.Exceptions.FullQueueException;
import java.util.ArrayList;

public class InputPort extends Thread {
    private String port_id;
    private Queue virtual_package_queue;
    private double package_generation_delay;
    private double drop_probability;
    private int count;
    private ArrayList<String> discarded_packages;
    private ArrayList<String> created_packages;
    private ArrayList<String> full_queue_packages;

    public InputPort(String port_id, int queue_size, double drop_probability, int package_generation_delay){
        this.port_id = port_id;
        virtual_package_queue = new Queue(queue_size);
        this.drop_probability = drop_probability;
        this.package_generation_delay = package_generation_delay;
        count = 0;
    }

    @Override
    public void run() {

    }

    protected VirtualPackage get_virtual_package() {
        return virtual_package_queue.pop();
    }

    private boolean generate_virtual_package() {
        VirtualPackage new_package = new VirtualPackage(this.port_id, this.count);

        double random_number = Math.random() * 100d;
        if (random_number < drop_probability) {
           discarded_packages.add(new_package.toString());
           return false;
        } else {
            created_packages.add(new_package.toString());
            try {
                virtual_package_queue.push(new_package);
                return true;
            } catch (FullQueueException fqe) {
                full_queue_packages.add(new_package.toString());
                return false;
            }
        }
    }

}
