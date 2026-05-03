package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;

/**
 * Команда вывода всех элементов коллекции.
 * Отображает все элементы в их строковом представлении.
 */
public class ShowCommand implements Command{
    private CollectionManager collectionManager;


    public ShowCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;

    }

    @Override
    public void execute(String args[], int userId){
        System.out.println(collectionManager.showElements());
    }

    @Override
    public String getDescription(){
        return "- Show all elements in the collection";
    }
}