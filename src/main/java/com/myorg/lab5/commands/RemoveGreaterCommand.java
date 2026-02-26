package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

public class RemoveGreaterCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public RemoveGreaterCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.removeGreater(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Remove all elements greater than the specified";
    }
}