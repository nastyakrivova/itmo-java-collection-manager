package com.myorg.lab5.io;

import java.util.Scanner;

import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ConsoleParser;

public class ConsoleManager {
    private ConsoleParser parser;
    private Scanner scanner;

    public ConsoleManager(){
        this.scanner = new Scanner(System.in);
        this.parser = new ConsoleParser();
    }

    public void show(String message){
        System.out.println(message);
    }

    public String read(){
        System.out.print(">");;
        return scanner.nextLine();
    }

    public MusicBand parse(){
        try{
            return parser.parse();
        } catch(Exception e) {
            System.err.println("Error parsing line: " + e.getMessage());
            return null;
        }
        
    }

}
