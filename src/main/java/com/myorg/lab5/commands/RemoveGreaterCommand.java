package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда удаления элементов, превышающих заданный.
 * Удаляет все элементы, которые больше указанного (согласно compareTo).
 */
public class RemoveGreaterCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    /**
     * Создает команду remove_greater.
     * 
     * @param collectionManager менеджер коллекции для удаления элементов
     * @param console консольный менеджер для ввода эталонного элемента
     */
    public RemoveGreaterCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.removeGreater(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Remove all elements greater than the specified";
    }
}