package com.myorg.lab7.commands;

import com.myorg.lab7.model.CollectionManager;

public class MyShowCommand implements Command {
    private final CollectionManager collectionManager;
    
    public MyShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args, int userId) {
        System.out.println(collectionManager.showUsersElements(userId));
    }
    
    @Override
    public String getDescription() {
        return "- Show only your music bands";
    }
}
