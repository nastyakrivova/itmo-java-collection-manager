package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

/**
 * Команда добавления элемента, если он меньше минимального элемента коллекции.
 * Сравнение происходит согласно естественному порядку (метод compareTo).
 */
public class AddIfMinCommand implements Command{
    private final CollectionManager collectionManager;
    private final ScriptParser lineParser = new ScriptParser();

    /**
     * Создает команду add_if_min.
     * 
     * @param collectionManager менеджер коллекции для проверки и добавления

     */
    public AddIfMinCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    
    /**
     * Выполняет команду add_if_min.
     * Сначала сортирует коллекцию, затем добавляет элемент только если он
     * меньше наименьшего текущего элемента.
     */
    @Override
    public void execute(String[] args, int userId){
        MusicBand band = lineParser.parse(args[0]);
        collectionManager.addIfMin(band);
    }

    @Override
    public String getDescription(){
        return "- Add element if it's smaller than the smallest";
    }
}
