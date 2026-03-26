package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

/**
 * Команда удаления элементов, меньших заданного.
 * Удаляет все элементы, которые меньше указанного (согласно compareTo).
 */
public class RemoveLowerCommand implements Command{
    private final CollectionManager collectionManager;
    private final ScriptParser lineParser = new ScriptParser();

    /**
     * Создает команду remove_lower.
     * 
     * @param collectionManager менеджер коллекции для удаления элементов
     */
    public RemoveLowerCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args){

        MusicBand band = lineParser.parse(args[0]);
        collectionManager.removeLower(band);
    }

    @Override
    public String getDescription(){
        return "- Remove all elements lower than the specified";
    }
}
