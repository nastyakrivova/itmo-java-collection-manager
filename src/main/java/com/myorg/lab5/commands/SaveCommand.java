package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

/**
 * Команда сохранения коллекции в файл.
 * Сохраняет текущее состояние коллекции в CSV файл,
 * указанный при запуске программы.
 */
public class SaveCommand implements Command{
    private CollectionManager collectionManager;

    public SaveCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String args[]){
        collectionManager.save();
    }

    @Override
    public String getDescription(){
        return "- Save the collection to a file";
    }
}
