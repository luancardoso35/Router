package com.company;

import java.util.ArrayList;

public class CommutingElement extends Thread{
    private long switch_delay;
    private ArrayList<InputPort> input_threads;
    private ArrayList<OutputPort> output_threads;
    private ArrayList<Double> probabilities_output_port;
    private boolean interrupted = false;

    /**
     * Construtor da classe comutador
     * @param switch_delay tempo de repasse do pacote
     * @param input_threads lista contendo os threads de input port
     * @param output_threads lista contendo os threads de output port
     */
    public CommutingElement(long switch_delay, ArrayList<InputPort> input_threads, ArrayList<OutputPort> output_threads) {
        this.switch_delay = switch_delay;
        this.input_threads = input_threads;
        this.output_threads = output_threads;
        probabilities_output_port = new ArrayList<>();
        for (OutputPort p : output_threads){
            probabilities_output_port.add(p.getPackage_forward_probability());
        }
    }

    @Override
    public void run() {
        while(!interrupted) {
            for (InputPort t: input_threads) {
                VirtualPackage vp = t.get_virtual_package();
                if (vp != null) {
                    int selected_output_port = sortOutputPort();
                    vp.setLocalDateTime(java.time.LocalDateTime.now());
                    try {
                        sleep(switch_delay);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    output_threads.get(selected_output_port).insert_package(vp);
                }
            }
        }
    }

    /**
     * Metodo responsavel por escolher uma porta de saida com base nas probabilidades de encaminhamento de cada uma
     * @return index da porta escolhida
     */
    private int sortOutputPort() {
        double probability_count = 0d;
        double sorted_number = Math.random() * 100d;

        for (int i = 0; i < probabilities_output_port.size(); i++) {
            probability_count += probabilities_output_port.get(i);
            if (sorted_number < probability_count) {
              return i;
            }
        }
        return -1;
    }

    /**
     * Metodo para parar o funcionamento do elemento comutador
     */
    public void stopThread(){
        interrupted = true;
    }

}
