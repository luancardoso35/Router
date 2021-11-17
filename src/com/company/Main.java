package com.company;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        //SYNCHRONIZED
        ThreadGroup input_threads = new ThreadGroup("input_threads");
        ThreadGroup output_threads = new ThreadGroup("output_threads");
        CommutingElement commuting_element;

        StringBuilder sb = readFile(args[1]);

        if (sb == null) {
            System.exit(-1);
        }

        if (check_pattern(sb)) {
            check_probabilities(sb);
//            commuting_element = create_commuting_element(sb);
            create_threads(input_threads, output_threads, sb);
        } else {
            System.out.println("Arquivo de texto fora da formatação");
            System.exit(-1);
        }
    }
    
    private static StringBuilder readFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
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
        Pattern p = Pattern.compile("switch-fabric:\\s\\d+\\n[input:\\s.+(\\s\\d+((\\.|,)\\d+)?){3}\\n]+" +
                "\\n[output:\\s.+(\\s\\d+((\\.|,)\\d+)?){4}\\n]+");
        Matcher m = p.matcher(sb);
        return m.matches();
    }

    private static void create_threads(ThreadGroup input_threads, ThreadGroup output_threads, StringBuilder sb){
        String[] lines = sb.toString().split("\\n");
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(" ");
            if (values[0].equals("input:")) {
                int size = Integer.parseInt(values[2]);
                double drop_prob = Double.parseDouble(values[2]);
                int delay = Integer.parseInt(values[2]);;
                Thread th = new Thread(input_threads, new InputPort(values[1],size, drop_prob, delay));
            } else {
                int size = Integer.parseInt(values[2]);
                double package_forward_probability = Double.parseDouble(values[3]);
                double package_transmission_delay = Double.parseDouble(values[4]);
                double retransmission_probability = Double.parseDouble(values[5]);
                Thread th = new Thread(output_threads, new OutputPort(values[1], size,
                        package_forward_probability, package_transmission_delay,
                        retransmission_probability));
            }
        }
    }

    //Confere se as probs sao validas
    private static void check_probabilities(StringBuilder text) {
        double count = 0d;  //Confere se soma da 100
        String[] lines = text.toString().split("\\n");
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
            if (count != 100d) {
                System.out.println("Probabilidades de transmissão das portas não somam 100%.");
                System.exit(-1);
            }
        }
    }

    private static void create_commuting_element(StringBuilder sb) {
        String first_line = sb.toString().split("\\n")[0];
//        return new CommutingElement(Double.parseDouble(first_line.split(" ")[1]));
    }

}
