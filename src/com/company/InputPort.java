package com.company;

import com.company.Exceptions.FullQueueException;

public class InputPort implements Runnable{
    private char port_id;
    private Queue virtual_package_queue;
    private int package_generation_delay;
    private double drop_probability;
    private int count;

    public InputPort(char port_id, int queue_size, double drop_probability){
        this.port_id = port_id;
        virtual_package_queue = new Queue(queue_size);
        this.drop_probability = drop_probability;
        count = 0;
    }

    @Override
    public void run() {

    }

    protected VirtualPackage get_virtual_package() {
        return virtual_package_queue.pop();
    }

    private void generate_virtual_package() {
        VirtualPackage new_package = new VirtualPackage(this.port_id, this.count);

        double random_number = Math.random() * 100d;
        if (random_number < drop_probability) {
           insert_package_in_discard_log(new_package);
        } else {
            insert_package_in_created_log(new_package);
            try {
                virtual_package_queue.push(new_package);
                insert_package_in_inserted_log(new_package);
            } catch (FullQueueException fqe) {
                insert_package_in_discard_log(new_package);
            }
        }
    }

    private void insert_package_in_created_log(VirtualPackage new_package) {

    }

    private void insert_package_in_inserted_log(VirtualPackage new_package) {

    }

    private void insert_package_in_discard_log(VirtualPackage new_package) {


    }


}
