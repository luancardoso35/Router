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

    /**
     * Construtor do InputPort
     * @param port_id id do InputPort
     * @param queue_size tamanho da fila de pacotes de entrada
     * @param drop_probability probabilidade de descarte de pacotes
     * @param package_generation_delay tempo de geracao de pacotes
     */
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
                System.out.println(e.getMessage());
            }
        }
        writeLogs();
    }

    /**
     * Metodo que retira um elemento da fila de entrada
     * @return o primeiro pacote da fila de entrada
     */
    protected VirtualPackage get_virtual_package() {
        return virtual_package_queue.pop();
    }

    /**
     * Cria pacotes e adiciona na fila, caso ela nao esteja cheia
     */
    private void generate_virtual_package() {
        VirtualPackage new_package = new VirtualPackage(this.port_id, this.count);
        count++;

        double random_number = Math.random() * 100d;

        //Caso haja erro, descarta o pacote e atualiza o log de pacotes descartados
        if (random_number < drop_probability) {
           discarded_packages.add(new_package.toString());
        } else {
            //Caso o pacote nao seja descartado, atualiza o log de pacotes criados e adiciona na fila
            created_packages.add(new_package.toString());
            try {
                virtual_package_queue.push(new_package);
            } catch (FullQueueException fqe) {
                //Caso a fila esteja cheia, descarta o pacote e atualiza log de pacotes nao adicionados na fila cheia
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
            System.out.println("Erro na escrita dos logs da porta de entrada.");
        }
    }

}
