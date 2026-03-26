package com.myorg.lab5.commands;


import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

/**
 * Команда добавления нового элемента в коллекцию.
 * Запрашивает у пользователя данные для создания объекта MusicBand
 * и добавляет его в коллекцию.
 */

public class AddCommand implements Command{
    private final CollectionManager collectionManager;
    private final ScriptParser lineParser = new ScriptParser();

    public AddCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    
    /**
     * Выполняет команду добавления.
     * Парсит введенные пользователем данные и добавляет новый элемент в коллекцию.
     */
    @Override
    public void execute(String[] args){
        MusicBand band = lineParser.parse(args[0]);
        collectionManager.add(band);
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
