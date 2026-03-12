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
        collectionManager.removeById(Integer.parseInt(args[0]));
    }

    @Override
    public String getDescription(){
        return "- Remove an element by its ID";
    }
}
