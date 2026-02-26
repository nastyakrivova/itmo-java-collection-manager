package com.myorg.lab5;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import com.myorg.lab5.commands.AddCommand;
import com.myorg.lab5.commands.AddIfMinCommand;
import com.myorg.lab5.commands.CommandManager;
import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.io.FileManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        String fileName = System.getenv("DATA");
        if(fileName == null){
            System.err.println("environment variable not found");
        }

        FileManager fileManager = new FileManager(fileName);
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();
        ConsoleManager consoleManager = new ConsoleManager();

        try{
            Collection<MusicBand> collection = fileManager.load();
            for (MusicBand band : collection) {
                collectionManager.add(band);
            }
        }catch (IOException e) {
            consoleManager.show("Could not load file: " + e.getMessage());
            consoleManager.show("Starting with empty collection");
        }


        commandManager.register("add", new AddCommand(collectionManager, consoleManager));
        commandManager.register("add_if_min", new AddIfMinCommand(collectionManager, consoleManager));
        
        while(true){
            String input = consoleManager.read();
            commandManager.execute(input);
        }
    }
}
