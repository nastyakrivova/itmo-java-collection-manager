package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

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
        collectionManager.clear();
    }

    @Override
    public String getDescription(){
        return "- Clear the entire collection";
    }
}
