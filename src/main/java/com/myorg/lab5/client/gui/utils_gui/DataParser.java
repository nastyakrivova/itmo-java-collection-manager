package com.myorg.lab5.client.gui.utils_gui;

import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.MusicBandParser;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    private final MusicBandParser parser = new MusicBandParser();
    
    public List<MusicBand> parseShowResponse(CommandResponse response) {
        List<MusicBand> bands = new ArrayList<>();
        
        if (!response.isSuccess() || response.getMessage() == null) {
            return bands;
        }
        
        String[] lines = response.getMessage().split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            try {
                MusicBand band = parser.parseFromString(line);
                bands.add(band);
            } catch (Exception e) {
                System.err.println("Parse error: " + line);
            }
        }
        return bands;
    }
    
    public String parseInfoResponse(CommandResponse response) {
        if (!response.isSuccess()) {
            return "Failed to get collection info";
        }
        return response.getMessage();
    }
    
    public int parseCountResponse(CommandResponse response) {
        if (!response.isSuccess()) {
            return 0;
        }
        try {
            return Integer.parseInt(response.getMessage().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}