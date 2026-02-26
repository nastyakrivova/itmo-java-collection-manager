package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

public class ClearCommand implements Command{
    private CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String args[]){
        collectionManager.clear();
    }

    @Override
    public String getDescription(){
        return "- Clear the entire collection";
    }
}
