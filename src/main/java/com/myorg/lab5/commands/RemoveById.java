package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда удаления элемента по ID.
 * Удаляет элемент коллекции с указанным идентификатором.
 */
public class RemoveById implements Command{
    private final CollectionManager collectionManager;
    private ConsoleManager consoleManager;

    public RemoveById(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }
    
    /**
     * Удаляет элемент с указанным ID.
     * 
     * @param args массив аргументов, где args[0] - ID элемента для удаления
     */
    @Override
    public void execute(String[] args){
        try {
            int id = Integer.parseInt(args[0]);
            boolean removed = collectionManager.removeById(id);
            
            if (removed) {
                consoleManager.show("Element with id " + id + " removed successfully");
            } else {
                consoleManager.show("Element with id " + id + " not found");
            }
        } catch (NumberFormatException e) {
            consoleManager.show("Error: ID must be a number");
        }
    }

    @Override
    public String getDescription(){
        return "- Remove an element by its ID";
    }
}
