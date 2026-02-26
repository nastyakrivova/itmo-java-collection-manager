package com.myorg.lab5.commands;


import java.util.Arrays;
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

    public boolean execute(String input){
        if(input == null || input.trim().isEmpty()){return false;}
        String[] line = input.trim().split("\\s+");
        String commandName = line[0];

        String[] args = Arrays.copyOfRange(line, 1, line.length);
    
        Command command = commands.get(commandName);
        if (command == null) { return false; }

        command.execute(args);
        return true;
    }

    public void printHelp(){
        commands.forEach((name, command) -> System.out.println(name + " " + command.getDescription()));
    }
}
