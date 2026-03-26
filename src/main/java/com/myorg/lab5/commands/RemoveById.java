package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

/**
 * Команда удаления элемента по ID.
 * Удаляет элемент коллекции с указанным идентификатором.
 */
public class RemoveById implements Command{
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
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
                System.out.println("Element with id " + id + " removed successfully");
            } else {
                System.out.println("Element with id " + id + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number");
        }
    }

    @Override
    public String getDescription(){
        return "- Remove an element by its ID";
    }
}
