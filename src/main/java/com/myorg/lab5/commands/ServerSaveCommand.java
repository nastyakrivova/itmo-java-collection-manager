package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

public class ServerSaveCommand implements Command{
    private final CollectionManager collectionManager;

    public ServerSaveCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args){
        try{
            collectionManager.save();
            System.out.println("Коллекция сохранена в файл");
        }catch(Exception e){
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "- Save collection to file";
    }
}
