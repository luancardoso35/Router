package com.company;

import com.company.Exceptions.FullQueueException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class InputPort extends Thread {
    private String port_id;
    private Queue virtual_package_queue;
    private long package_generation_delay;
    private double drop_probability;
    private int count;
    private ArrayList<String> discarded_packages;
    private ArrayList<String> created_packages;
    private ArrayList<String> full_queue_packages;
    private boolean interrupted = false;

    public InputPort(String port_id, int queue_size, double drop_probability, int package_generation_delay){
        this.port_id = port_id;
        virtual_package_queue = new Queue(queue_size);
        this.drop_probability = drop_probability;
        this.package_generation_delay = package_generation_delay;
        count = 0;
        discarded_packages = new ArrayList<>();
        created_packages = new ArrayList<>();
        full_queue_packages = new ArrayList<>();

    }

    @Override
    public void run() {
        while(!interrupted) {
            try {
                sleep(package_generation_delay);
                this.generate_virtual_package();
            } catch (InterruptedException e) {
            }
        }
        writeLogs();
    }

    protected VirtualPackage get_virtual_package() {
        return virtual_package_queue.pop();
    }

    private void generate_virtual_package() {
        VirtualPackage new_package = new VirtualPackage(this.port_id, this.count);
        count++;

        double random_number = Math.random() * 100d;
        if (random_number < drop_probability) {
           discarded_packages.add(new_package.toString());
        } else {
            created_packages.add(new_package.toString());
            try {
                virtual_package_queue.push(new_package);
            } catch (FullQueueException fqe) {
                full_queue_packages.add(new_package.toString());
            }
        }
    }

    public void stopThread(){
        interrupted = true;
    }
    public void writeLogs() {
        try (FileWriter fw = new FileWriter("pacotes_criados_com_sucesso" + port_id + ".txt");
             FileWriter fw2 = new FileWriter("pacotes_descartados" + port_id + ".txt");
             FileWriter fw3 = new FileWriter("pacotes_fila_cheia" + port_id + ".txt")) {
            for (String s: created_packages) {
                fw.write(s + "\n");
            }
            for (String s: discarded_packages) {
                fw2.write(s + "\n");
            }
            for (String s: full_queue_packages) {
                fw3.write(s + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
