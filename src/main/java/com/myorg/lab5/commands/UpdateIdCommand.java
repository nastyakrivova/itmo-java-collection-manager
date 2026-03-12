package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

/**
 * Команда обновления элемента по ID.
 * Заменяет элемент с указанным ID новыми данными.
 */
public class UpdateIdCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public UpdateIdCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    /**
     * Обновляет элемент с указанным ID.
     * 
     * @param args массив аргументов, где args[0] - ID обновляемого элемента
     */
    @Override
    public void execute(String[] args){
        MusicBand updatedMusicBand = parser.parse();
        collectionManager.updateId(Integer.parseInt(args[0]), updatedMusicBand);
    }

    @Override
    public String getDescription(){
        return "- Update an element by its ID";
    }
}
