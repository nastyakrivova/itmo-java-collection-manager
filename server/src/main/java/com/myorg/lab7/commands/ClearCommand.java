package com.myorg.lab7.commands;

import com.myorg.lab7.model.CollectionManager;
import com.myorg.lab7.model.CollectionManager.OperationResult;

/**
 * Команда очистки коллекции.
 * Удаляет все элементы из коллекции.
 */
public class ClearCommand implements Command{
    private CollectionManager collectionManager;

    /**
     * Создает команду очистки.
     * 
     * @param collectionManager менеджер коллекции для очистки
     */
    public ClearCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String args[], int userId){
        OperationResult result = collectionManager.clear(userId);
        switch (result) {
            case NOT_FOUND:
                System.out.println("You have no bands to remove");
                break;
            case SUCCESS:
                System.out.println("All your bands have been removed successfully");
                break;
            default:
                System.out.println("Error: Could not clear your bands");
        }
    }

    @Override
    public String getDescription(){
        return "- Clear the entire collection";
    }
}
