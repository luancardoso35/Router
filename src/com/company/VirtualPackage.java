package com.company;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VirtualPackage {
    private char inputPort;
    private int id;
    private LocalDateTime localDateTime;
    
    public VirtualPackage(char inputPort, int id) {
        this.inputPort = inputPort;
        this.id = id;
        this.localDateTime = java.time.LocalDateTime.now();
    }

    public String toString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dados = inputPort + "_" + id + " " + localDateTime.format(dtf);
        return dados;
    }
    
    private String parseDateTime(LocalDateTime dateTime) {
        String[] dateTimeSplit = dateTime.toString().split("T");
        String dateTimeAsString = dateTimeSplit[0] + dateTimeSplit[1];

        return dateTimeAsString;
    }


}
