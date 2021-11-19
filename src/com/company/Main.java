package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        ArrayList<InputPort> input_threads = new ArrayList<>();
        ArrayList<OutputPort> output_threads = new ArrayList<>();
        CommutingElement commuting_element = null;
        StringBuilder sb = readFile(args[0]);

        if (sb == null) {
            System.exit(-1);
        }

        if (check_pattern(sb)) {
            check_probabilities(sb);
            create_threads(input_threads, output_threads, sb);
            commuting_element = create_commuting_element(sb, input_threads, output_threads);
            commuting_element.start();
        } else {
            System.out.println("Arquivo de texto fora da formatação");
            System.exit(-1);
        }
        System.out.println("Digite s para sair");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        while (!in.equals("s")) {
            in = sc.nextLine();
        }

        commuting_element.stopThread();
        for (InputPort ip: input_threads) {
            ip.stopThread();
            try {
                ip.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        for (OutputPort op: output_threads) {
            op.stopThread();
            try {
                op.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.exit(0);
    }
    
    private static StringBuilder readFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();

            int value;
            while ((value = br.read()) != -1) {
                char c = (char)value;
                sb.append(c);
            }

            if (sb.charAt(sb.length()-1) != '\n') {
                System.out.println("Arquivo de texto fora da formatação");
                System.exit(-1);
            }

            return sb;
        } catch (FileNotFoundException fnfe) {
            System.out.println("Arquivo não encontrado");
            return null;
        } catch (IOException ioe) {
            System.out.println("Erro na leitura");
            return null;
        }
    }

    private static boolean check_pattern(StringBuilder sb) {
        Pattern p = Pattern.compile("switch-fabric:\\s\\d+\\r?\\n[input:\\s\\w+(\\s\\d+((.|,)\\d+)?){3}\\r?\\n]+" +
                "[output:\\s\\w+(\\s\\d+((\\.|,)\\d+)?){4}\\r?\\n]+");
        Matcher m = p.matcher(sb);
        return m.matches();
    }

    private static void create_threads(ArrayList<InputPort> input_threads, ArrayList<OutputPort> output_threads,
                                       StringBuilder sb){
        String[] lines = sb.toString().split("\\n");
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(" ");
            if (values[0].equals("input:")) {
                int size = Integer.parseInt(values[2]);
                double drop_prob = Double.parseDouble(values[2]);
                long delay = Long.parseLong(values[2]);
                InputPort ip = new InputPort(values[1],size, drop_prob, delay);
                ip.start();
                input_threads.add(ip);
            } else {
                int size = Integer.parseInt(values[2]);
                double package_forward_probability = Double.parseDouble(values[3]);
                long package_transmission_delay = Long.parseLong(values[4]);
                double retransmission_probability = Double.parseDouble(values[5]);
                OutputPort op = new OutputPort(values[1], size,
                        package_forward_probability, package_transmission_delay,
                        retransmission_probability);
                op.start();
                output_threads.add(op);
            }
        }
    }

    //Confere se as probs sao validas
    private static void check_probabilities(StringBuilder text) {
        double count = 0d;  //Confere se soma da 100
        String[] lines = text.toString().split("\\r?\\n");
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(" ");
            if (values[0].equals("input:")) {
                double drop_prob = Double.parseDouble(values[2]);
                if (drop_prob > 100d) {
                    System.out.println("Probabilidade maior que 100%.");
                    System.exit(-1);
                }
            } else {
                double package_forward_probability = Double.parseDouble(values[3]);
                double retransmission_probability = Double.parseDouble(values[5]);

                if (package_forward_probability > 100d || retransmission_probability > 100d) {
                    System.out.println("Probabilidade maior que 100%.");
                    System.exit(-1);
                } else {
                    count += package_forward_probability;
                }
            }
        }
        if (count != 100d) {
            System.out.println("Probabilidades de transmissão das portas não somam 100%.");
            System.exit(-1);
        }
    }

    private static CommutingElement create_commuting_element(StringBuilder sb, ArrayList<InputPort> input_threads,
                                                             ArrayList<OutputPort> output_threads) {
        String first_line = sb.toString().split("\\n")[0];
        return new CommutingElement(Long.parseLong(first_line.split(" ")[1]),
                input_threads, output_threads);
    }

}
