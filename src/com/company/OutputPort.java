package com.company;

import com.company.Exceptions.FullQueueException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class OutputPort extends Thread{
    private String port_id;
    private Queue virtual_package_queue;
    private double package_forward_probability;
    private long package_transmission_delay;
    private double retransmission_probability;
    private double package_retransmission_probability;
    private ArrayList<String> relayed_packages;
    private ArrayList<String> sent_packages;
    private boolean interrupted = false;


    public OutputPort(String port_id, int size, double package_forward_probability,
                      long package_transmission_delay, double retransmission_probability) {
        this.port_id = port_id;
        this.package_forward_probability = package_forward_probability;
        this.package_transmission_delay = package_transmission_delay;
        this.package_retransmission_probability = retransmission_probability;
        this.retransmission_probability = retransmission_probability;
        this.virtual_package_queue = new Queue(size);
        relayed_packages = new ArrayList<>();
        sent_packages = new ArrayList<>();
    }

    @Override
    public void run() {
        while(!interrupted) {
            try {
                sleep(package_transmission_delay);
                send();
            } catch (InterruptedException e) {
            }
        }
        writeLogs();
    }

    private void send () {
        double random_number = Math.random()*100d;
        double success_probability = 100d - package_retransmission_probability;

        if (virtual_package_queue.size() != 0) {
            if (random_number < success_probability) {
                VirtualPackage virtual_package = virtual_package_queue.pop();
                if (virtual_package == null) {
                    System.out.println("x");
                }
                virtual_package.setLocalDateTime(java.time.LocalDateTime.now());
                sent_packages.add(virtual_package.toString());
                this.package_retransmission_probability = this.retransmission_probability;
            } else {
                VirtualPackage vp = virtual_package_queue.getFirst();
                vp.setLocalDateTime(java.time.LocalDateTime.now());
                relayed_packages.add(vp.toString());
                this.package_retransmission_probability /= 2;
            }
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

    public void stopThread() {
        interrupted = true;
    }


    public void writeLogs() {
        try (FileWriter fw = new FileWriter("pacotes_enviados_com_sucesso" + port_id + ".txt");
             FileWriter fw2 = new FileWriter("pacotes_reenviados" + port_id + ".txt");
             FileWriter fw3 = new FileWriter("pacotes_nao_tratados" + port_id + ".txt")) {
            for (String s: relayed_packages) {
                fw.write(s + "\n");
            }
            for (String s: sent_packages) {
                fw2.write(s + "\n");
            }

            while(virtual_package_queue.size() != 0) {
                VirtualPackage vp = virtual_package_queue.pop();
                fw3.write(vp.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
