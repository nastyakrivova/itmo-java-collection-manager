package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.Studio;

public class CountByStudioCommand implements Command {
    private CollectionManager collectionManager;
    private final ConsoleManager consoleManager;

    public CountByStudioCommand(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public void execute(String args[]){
        consoleManager.show(String.valueOf(collectionManager.countByStudio(new Studio(args[0]))));
    }

    @Override
    public String getDescription(){
        return "- Count elements with the specified studio";
    }
}
