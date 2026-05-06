package com.myorg.lab5.commands;

import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.CollectionManager.OperationResult;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

public class UpdateIdCommand implements Command {
    private final CollectionManager collectionManager;
    private final ScriptParser parser = new ScriptParser();

    public UpdateIdCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public void execute(String[] args, int userId) {
        if (args == null || args.length < 2) {
            System.out.println("Error: update <id> <band_data>");
            return;
        }
        
        try {
            int id = Integer.parseInt(args[0].trim());
            String bandData = args[1];
            
            MusicBand newBandData = parser.parse(bandData);
            if (newBandData == null) {
                System.out.println("Error: Invalid band data format");
                return;
            }
            
            MusicBand existing = collectionManager.getBandById(id);
            if (existing == null) {
                System.out.println("Element with id " + id + " not found");
                return;
            }
            if (existing.getOwnerId() != userId) {
                System.out.println("Access denied: You are not the owner of element " + id);
                return;
            }
            
            OperationResult result = collectionManager.updateId(id, newBandData, userId);
            if (result == OperationResult.SUCCESS) {
                System.out.println("Element with id " + id + " updated successfully");
            } else {
                System.out.println("Error: Could not update element " + id);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "- Update an element by its ID";
    }
}