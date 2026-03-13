package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда сохранения коллекции в файл.
 * Сохраняет текущее состояние коллекции в CSV файл,
 * указанный при запуске программы.
 */
public class SaveCommand implements Command{
    private CollectionManager collectionManager;
    private ConsoleManager consoleManager;

    public SaveCommand(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public void execute(String args[]){
        boolean saved = collectionManager.save();
        if (saved) {
            consoleManager.show("Collection saved successfully");
        } else {
            consoleManager.show("Error saving collection");
        }
    }

    @Override
    public String getDescription(){
        return "- Save the collection to a file";
    }
}
