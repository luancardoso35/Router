package com.company;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VirtualPackage {
    private String inputPort;
    private int id;
    private LocalDateTime localDateTime;

    /**
     * Construtor da classe VirtualPackage
     * @param inputPort o identificador da porta de entrada a qual aquele pacote está associado
     * @param id identificador único do pacote virtual
     */
    public VirtualPackage(String inputPort, int id) {
        this.inputPort = inputPort;
        this.id = id;
        this.localDateTime = java.time.LocalDateTime.now();
    }

    /**
     * Método para obtenção da representação textual do pacote
     * @return representação textual do pacote, contendo a porta associada, o identificador
     * e o marcador temporal associado
     */
    public String toString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dados = inputPort + id + " " + this.getLocalDateTime().format(dtf);
        return dados;
    }

    /**
     * Método para obtenção do marcador temporal associado àquele pacote
     * @return marcador temporal daquele pacote
     */
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Método para alteração do marcador temporal associado ao pacote
     * @param localDateTime o novo marcador temporal que será associado ao pacote
     */
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
