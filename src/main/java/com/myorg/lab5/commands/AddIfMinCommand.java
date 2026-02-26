package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

public class AddIfMinCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public AddIfMinCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.addIfMin(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Add element if it's smaller than the smallest";
    }
}
