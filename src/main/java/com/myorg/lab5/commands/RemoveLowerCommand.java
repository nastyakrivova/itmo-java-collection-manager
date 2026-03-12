package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда удаления элементов, меньших заданного.
 * Удаляет все элементы, которые меньше указанного (согласно compareTo).
 */
public class RemoveLowerCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    /**
     * Создает команду remove_lower.
     * 
     * @param collectionManager менеджер коллекции для удаления элементов
     * @param console консольный менеджер для ввода эталонного элемента
     */
    public RemoveLowerCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.removeLower(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Remove all elements lower than the specified";
    }
}
