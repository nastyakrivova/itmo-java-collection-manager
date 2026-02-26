package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

public class UpdateIdCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public UpdateIdCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
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
