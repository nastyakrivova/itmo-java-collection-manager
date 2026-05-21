package com.myorg.lab5.client.gui.drawing;

import com.myorg.lab5.model.MusicBand;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class BandDrawer {
    private final Canvas canvas;
    private final ColorProvider colorProvider;
    
    public BandDrawer(Canvas canvas, int currentUserId) {
        this.canvas = canvas;
        this.colorProvider = new ColorProvider(currentUserId);
    }
    
    public void setCurrentUserId(int userId) {
        colorProvider.setCurrentUserId(userId);
    }
    
    public void draw(List<MusicBand> bands) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (MusicBand band : bands) {
            double x = band.getCoordinates().getX();
            double y = band.getCoordinates().getY();
            double size = 10 + band.getNumberOfParticipants() / 4;
            Color color = colorProvider.getColorForOwner(band.getOwnerId());
            
            gc.setFill(color);
            gc.fillOval(x, y, size, size);
            
            gc.setFill(Color.BLACK);
            gc.fillText(band.getName(), x, y - 5);
        }
    }
    
    public MusicBand findBandAt(List<MusicBand> bands, double x, double y) {
        for (MusicBand band : bands) {
            double bandX = band.getCoordinates().getX();
            double bandY = band.getCoordinates().getY();
            double size = 10 + band.getNumberOfParticipants() / 4;
            
            if (x >= bandX && x <= bandX + size && y >= bandY && y <= bandY + size) {
                return band;
            }
        }
        return null;
    }

    public Color getColorForOwner(int ownerId) {
        return colorProvider.getColorForOwner(ownerId);
    }
}