package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда добавления элемента, если он меньше минимального элемента коллекции.
 * Сравнение происходит согласно естественному порядку (метод compareTo).
 */
public class AddIfMinCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    /**
     * Создает команду add_if_min.
     * 
     * @param collectionManager менеджер коллекции для проверки и добавления
     * @param console консольный менеджер для ввода данных
     */
    public AddIfMinCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    /**
     * Выполняет команду add_if_min.
     * Сначала сортирует коллекцию, затем добавляет элемент только если он
     * меньше наименьшего текущего элемента.
     */
    @Override
    public void execute(String[] args){
        collectionManager.addIfMin(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Add element if it's smaller than the smallest";
    }
}
