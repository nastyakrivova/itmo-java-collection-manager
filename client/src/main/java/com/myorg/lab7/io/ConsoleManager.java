package com.myorg.lab7.io;

import java.util.Scanner;
import com.myorg.lab7.model.MusicBand;
import com.myorg.lab7.utils.MusicBandParser;

public class ConsoleManager {
    private final Scanner scanner;
    private final MusicBandParser parser;
    
    public ConsoleManager(Scanner scanner) {
        this.scanner = scanner;
        this.parser = new MusicBandParser(scanner);
    }
    
    public void show(String message) {
        System.out.println(message);
    }
    
    public String read() {
        System.out.print("> ");
        return scanner.nextLine();
    }
    
    public String read(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    public MusicBand parse() {
        return parser.parseInteractively();
    }
}