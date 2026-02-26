package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

public class RemoveLowerCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public RemoveLowerCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.removeLower(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Remove all elements lower than the specified";
    }
}
