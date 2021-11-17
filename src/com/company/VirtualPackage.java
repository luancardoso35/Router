package com.company;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VirtualPackage {
    private String inputPort;
    private int id;
    private LocalDateTime localDateTime;
    
    public VirtualPackage(String inputPort, int id) {
        this.inputPort = inputPort;
        this.id = id;
        this.localDateTime = java.time.LocalDateTime.now();
    }

    public String toString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dados = inputPort + id + " " + this.getLocalDateTime().format(dtf);
        return dados;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
