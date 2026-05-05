package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

public class AddCommand implements Command {
    private final CollectionManager collectionManager;
    private final ScriptParser parser = new ScriptParser();

    public AddCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args, int userId) {
        if (args == null || args.length == 0) {
            System.out.println("Error: No band data provided");
            return;
        }
        
        System.out.println("=== ADD COMMAND DEBUG ===");//cut!!
        System.out.println("args.length = " + args.length);//cut
        for (int i = 0; i < args.length; i++) {//cut
            System.out.println("args[" + i + "] = \"" + args[i] + "\"");//cut
        }//cut

        try {
            String bandData = args[0];
            System.out.println("Received: " + bandData);//cut!!!
            MusicBand band = parser.parse(bandData);

            if (band == null) {
                System.out.println("Error: Invalid band data format");
                return;
            }
            
            boolean success = collectionManager.add(band, userId);
            if (success) {
                System.out.println("Band added successfully with ID: " + band.getId());
            } else {
                System.out.println("Error: Failed to add band");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "- Add a new element to the collection";
    }
}