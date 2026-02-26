package com.myorg.lab5.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands;
    public CommandManager(){
        this.commands = new HashMap<>();
    }
    public void register(String name, Command command){
        commands.put(name, command);
    }

    public boolean execute(String args[]){
        return true;
    }

    public void printHelp(){
        commands.forEach((name, command) -> System.out.println(name + " " + command.getDescription()));
    }
}
