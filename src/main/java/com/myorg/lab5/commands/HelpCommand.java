package com.myorg.lab5.commands;


public class HelpCommand implements Command{
    private final CommandManager commandManager;
    public HelpCommand(CommandManager commandManager){
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String arg[]){
        commandManager.printHelp();
    }

    @Override
    public String getDescription(){
        return "- Display this help message";
    }
}