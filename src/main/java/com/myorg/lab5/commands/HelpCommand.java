package com.myorg.lab5.commands;

// conected to command manager
// обращается через менеджер команд через мапу к команде getDescription
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