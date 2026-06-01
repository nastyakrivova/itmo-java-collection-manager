package com.myorg.lab7.client;

import com.myorg.lab7.io.ConsoleManager;

public class ExitCommand {
    private final ConsoleManager consoleManager;
    
    public ExitCommand(ConsoleManager consoleManager) {
        this.consoleManager = consoleManager;
    }
    
    public void execute() {
        consoleManager.show("Exiting...");
        System.exit(0);
    }
}