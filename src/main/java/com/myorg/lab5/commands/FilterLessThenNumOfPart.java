package com.myorg.lab5.commands;

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;


public class FilterLessThenNumOfPart implements Command{
    private CollectionManager collectionManager;
    private final ConsoleManager consoleManager;

    public FilterLessThenNumOfPart(CollectionManager collectionManager, ConsoleManager consoleManager){
        this.collectionManager = collectionManager;
        this.consoleManager = consoleManager;
    }

    @Override
    public void execute(String args[]){
        consoleManager.show(collectionManager.filterLessThanNumbOfParticipants(Integer.parseInt(args[0])).toString());
    }

    @Override
    public String getDescription(){
        return "- Show elements with participants < N";
    }
}
