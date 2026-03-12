package com.myorg.lab5.commands;


import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.io.ConsoleManager;

/**
 * Команда добавления нового элемента в коллекцию.
 * Запрашивает у пользователя данные для создания объекта MusicBand
 * и добавляет его в коллекцию.
 */

public class AddCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public AddCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    /**
     * Выполняет команду добавления.
     * Парсит введенные пользователем данные и добавляет новый элемент в коллекцию.
     */
    @Override
    public void execute(String[] args){
        collectionManager.add(parser.parse());
    }

    /**
     * Возвращает описание команды для справки.
     * 
     * @return строковое описание команды
     */
    @Override
    public String getDescription(){
        return "- Add a new element to the collection";
    }
}
