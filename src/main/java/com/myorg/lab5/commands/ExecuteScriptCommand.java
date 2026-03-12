package com.myorg.lab5.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.myorg.lab5.io.ConsoleManager;

public class ExecuteScriptCommand implements Command {
    private final CommandManager commandManager;
    private final ConsoleManager consoleManager;
    private static final Set<String> executingScripts = new HashSet<>();

    public ExecuteScriptCommand(CommandManager commandManager, ConsoleManager consoleManager) {
        this.commandManager = commandManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public String getDescription() {
        return "- Execute commands from script file (usage: execute_script filename)";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("File name not specified");
            return;
        }

        String fileName = args[0];

        if (executingScripts.contains(fileName)) {
            System.out.println("Recursion is prohibited");
            return;
        }

        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File not found");
            return;
        }

        executingScripts.add(fileName);


        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             Scanner fileScanner = new Scanner(bis)) {

            consoleManager.enableScriptMode(fileScanner);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                System.out.println("→ " + line);
                commandManager.execute(line);
            }

        } catch (Exception e) {
            System.out.println("Ошибка выполнения скрипта: " + e.getMessage());
        } finally {
            consoleManager.disableScriptMode();
            executingScripts.remove(fileName);
        }
    }
}