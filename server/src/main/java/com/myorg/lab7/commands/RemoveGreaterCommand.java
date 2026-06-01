package com.myorg.lab7.commands;

import com.myorg.lab7.model.CollectionManager;
import com.myorg.lab7.model.CollectionManager.OperationResult;
import com.myorg.lab7.model.MusicBand;
import com.myorg.lab7.utils.ScriptParser;

/**
 * Команда удаления элементов, превышающих заданный.
 * Удаляет все элементы, которые больше указанного (согласно compareTo).
 */
public class RemoveGreaterCommand implements Command{
    private final CollectionManager collectionManager;
    private final ScriptParser lineParser = new ScriptParser();

    /**
     * Создает команду remove_greater.
     * 
     * @param collectionManager менеджер коллекции для удаления элементов
     */
    public RemoveGreaterCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args, int userId){
        MusicBand band = lineParser.parse(args[0]);
        OperationResult result = collectionManager.removeGreater(band, userId);
        switch (result) {
            case NOT_FOUND:
                System.out.println("No greater bands found");
                break;
            case SUCCESS:
                System.out.println("Greater bands removed successfully");
                break;
            default:
                System.out.println("Error removing greater bands");
        }
    }

    @Override
    public String getDescription(){
        return "- Remove all elements greater than the specified";
    }
}