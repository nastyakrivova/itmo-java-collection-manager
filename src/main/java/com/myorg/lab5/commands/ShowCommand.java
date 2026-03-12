package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;

/**
 * Команда вывода всех элементов коллекции.
 * Отображает все элементы в их строковом представлении.
 */
public class ShowCommand implements Command{
    private CollectionManager collectionManager;
    private ConsoleManager consoleManager;

    public ShowCommand(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public void execute(String args[]){
        consoleManager.show(collectionManager.showElements());
    }

    @Override
    public String getDescription(){
        return "- Show all elements in the collection";
    }
}