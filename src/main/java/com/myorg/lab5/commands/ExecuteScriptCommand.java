package com.myorg.lab5.commands;


import com.myorg.lab5.utils.ScriptParser;

/**
 * Команда выполнения скрипта из файла.
 * Читает команды из указанного файла и выполняет их последовательно.
 * Защищает от рекурсивного выполнения скриптов.
 * Использует BufferedInputStream для чтения файла согласно требованиям.
 */
public class ExecuteScriptCommand implements Command{
    private ScriptParser scriptParser;

    public ExecuteScriptCommand(ScriptParser scriptParser){
        this.scriptParser = scriptParser;
    }
    
    /**
     * Выполняет скрипт из файла.
     * Переключает парсер в режим чтения из файла, выполняет все команды,
     * затем возвращает парсер в консольный режим.
     * 
     * @param args массив аргументов, где args[0] - имя файла скрипта
     */
    @Override
    public void execute(String[] args){
        if (args.length < 1) {
            System.out.println("Error: Please specify script file name");
            return;
        }
        scriptParser.parse(args[0]);
    }

    @Override
    public String getDescription(){
        return "-Execute commands from script file (usage: execute_script filename)";
    }
}
