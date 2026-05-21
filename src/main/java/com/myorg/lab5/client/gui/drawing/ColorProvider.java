package com.myorg.lab5.client.gui.drawing;

import javafx.scene.paint.Color;

public class ColorProvider {
    private int currentUserId;
    
    public ColorProvider(int currentUserId) {
        this.currentUserId = currentUserId;
    }
    
    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }
    
    public Color getColorForOwner(int ownerId) {
        if (ownerId == currentUserId) {
            return Color.LIGHTGREEN;
        }
        double hue = (ownerId * 137) % 360;
        return Color.hsb(hue, 0.6, 0.9);
    }
}