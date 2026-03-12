package com.myorg.lab5.io;

import java.io.*;
import java.util.*;
import com.myorg.lab5.commands.CommandManager;

public class ScriptExecutor {
    private final CommandManager commandManager;
    private final ConsoleManager consoleManager;
    private Set<String> executingScripts = new HashSet<>();

    public ScriptExecutor(CommandManager commandManager, ConsoleManager consoleManager) {
        this.commandManager = commandManager;
        this.consoleManager = consoleManager;
    }

    public void execute(String fileName) {
        File file = new File(fileName);
        
        if (executingScripts.contains(file.getAbsolutePath())) {
            consoleManager.show("Error: Recursive script execution detected");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            executingScripts.add(file.getAbsolutePath());
            consoleManager.show("Executing script: " + fileName);
            
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                consoleManager.show("→ " + line);
                commandManager.execute(line);
            }
            
        } catch (FileNotFoundException e) {
            consoleManager.show("Error: File not found: " + fileName);
        } catch (IOException e) {
            consoleManager.show("Error reading script: " + e.getMessage());
        } finally {
            executingScripts.remove(file.getAbsolutePath());
        }
    }
}