package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

public class RemoveById implements Command{
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.removeById(Integer.parseInt(args[0]));
    }

    @Override
    public String getDescription(){
        return "- Remove an element by its ID";
    }
}
