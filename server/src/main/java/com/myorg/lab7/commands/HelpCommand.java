package com.myorg.lab7.commands;

/**
 * Команда вывода справки по доступным командам.
 * Показывает список всех команд с их описаниями.
 */
public class HelpCommand implements Command{
    private final CommandManager commandManager;
    /**
     * Создает команду помощи.
     * 
     * @param commandManager менеджер команд для получения списка команд
     */
    public HelpCommand(CommandManager commandManager){
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String arg[], int userId){
        commandManager.printHelp();
    }

    @Override
    public String getDescription(){
        return "- Display this help message";
    }
}