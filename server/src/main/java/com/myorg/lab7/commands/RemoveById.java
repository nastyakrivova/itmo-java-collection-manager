package com.myorg.lab7.commands;

import com.myorg.lab7.model.CollectionManager;
import com.myorg.lab7.model.CollectionManager.OperationResult;

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
    public void execute(String[] args, int userId){
        try {
            int id = Integer.parseInt(args[0]);
            OperationResult result = collectionManager.removeById(id, userId);
            
            switch (result) {
                case NOT_FOUND:
                    System.out.println("Element with id " + id + " not found");
                    break;
                case NOT_OWNER:
                    System.out.println("Access denied: You are not the owner of element " + id);
                    break;
                case SUCCESS:
                    System.out.println("Element with id " + id + " removed successfully");
                    break;
            
                default:
                    System.out.println("Error: Could not remove element " + id);
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
