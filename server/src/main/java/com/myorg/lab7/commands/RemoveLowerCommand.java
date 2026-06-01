package com.myorg.lab7.commands;

import com.myorg.lab7.model.CollectionManager;
import com.myorg.lab7.model.CollectionManager.OperationResult;
import com.myorg.lab7.model.MusicBand;
import com.myorg.lab7.utils.ScriptParser;

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
    public void execute(String[] args, int userId){

        MusicBand band = lineParser.parse(args[0]);
        OperationResult result = collectionManager.removeLower(band, userId);
        switch (result) {
            case NOT_FOUND:
                System.out.println("No lower bands found");
                break;
            case SUCCESS:
                System.out.println("Lower bands removed successfully");
                break;
            default:
                System.out.println("Error removing lower bands");
        }
    }

    @Override
    public String getDescription(){
        return "- Remove all elements lower than the specified";
    }
}
