package com.myorg.lab5.commands;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Менеджер команд приложения.
 * Регистрирует, хранит и выполняет команды по их именам.
 * Реализует паттерн "Команда" для централизованного управления.
 */
public class CommandManager {
    private final Map<String, Command> commands;
    public CommandManager(){
        this.commands = new HashMap<>();
    }

    /**
     * Регистрирует новую команду в менеджере.
     * Команда становится доступной для выполнения по указанному имени.
     * 
     * @param name имя команды (например, "add", "help")
     * @param command объект команды, реализующий интерфейс Command
     */
    public void register(String name, Command command){
        commands.put(name, command);
    }

    /**
     * Выполняет команду по строке ввода.
     * Разбирает входную строку на имя команды и аргументы,
     * находит соответствующую команду и выполняет её.
     * 
     * @param input строка ввода от пользователя или из скрипта
     */
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

    public boolean execute(String commandName, String[] args){
        Command command = commands.get(commandName);
        if (command == null) {
            return false;
        }
        command.execute(args);
        return true;
    }

    public String executeAndGetResult(String commanddName, String[] args) {
        Command command = commands.get(commanddName);
        if(command == null) {
            return null;
        }
        return command.executeAndReturn(args);
    }

    public void printHelp(){
        commands.forEach((name, command) -> System.out.println(name + " " + command.getDescription()));
    }
}
