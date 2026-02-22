package com.myorg.lab5.commands;

import java.util.Scanner;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.utils.MusicBandParser;

public class AddCommand implements Command{
    private final CollectionManager collectionManager;
    private final MusicBandParser parser;

    public AddCommand(CollectionManager collectionManager, MusicBandParser parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    @Override
    public void execute(String[] args){
        collectionManager.add(parser.parse());
    }

    @Override
    public String getDescription(){
        return "- Add a new element to the collection";
    }
}
