package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

public class CheckUpdateCommand implements Command {
    private final CollectionManager collectionManager;
    
    public CheckUpdateCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args, int userId) {
        if (args == null || args.length == 0) {
            System.out.println("ERROR: No ID provided");
            return;
        }
        
        try {
            int id = Integer.parseInt(args[0].trim());
            MusicBand existing = collectionManager.getBandById(id);
            
            if (existing == null) {
                System.out.println("NOT_FOUND:" + id);
                return;
            }
            if (existing.getOwnerId() != userId) {
                System.out.println("NOT_OWNER:" + id);
                return;
            }
            System.out.println("OK:" + id);
            
        } catch (NumberFormatException e) {
            System.out.println("ERROR: ID must be a number");
        }
    }
    
    @Override
    public String getDescription() {
        return "- Check if user can update an element (internal)";
    }
}