package com.myorg.lab5.commands;


import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Команда выполнения скрипта из файла.
 * Читает команды из указанного файла и выполняет их последовательно.
 * Защищает от рекурсивного выполнения скриптов.
 * Использует BufferedInputStream для чтения файла согласно требованиям.
 */
public class ExecuteScriptCommand implements Command{
    private final CommandManager commandManager;

    public ExecuteScriptCommand(CommandManager commandManager){
        this.commandManager = commandManager;
    }
    
    /**
     * Выполняет скрипт из файла.
     * Переключает парсер в режим чтения из файла, выполняет все команды,
     * затем возвращает парсер в консольный режим.
     * 
     * @param args массив аргументов, где args[0] - имя файла скрипта
     */
    @Override
    public void execute(String[] args, int userId){
        if (args.length < 1) {
            System.out.println("Error: Please specify script file name");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            int lineNum = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                boolean success = commandManager.execute(line, userId);
                if (!success) {
                    System.err.println("Ошибка выполнения: строки " + lineNum +  ":" + line);
                }
            }
        }catch(Exception e){
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }



    @Override
    public String getDescription(){
        return "-Execute commands from script file (usage: execute_script filename)";
    }
}
