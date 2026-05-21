package com.myorg.lab5.client.gui.drawing;

import com.myorg.lab5.model.MusicBand;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class AnimationManager {
    
    private final Canvas canvas;
    private final Consumer<GraphicsContext> baseDrawer;  // функция отрисовки всех объектов
    private final Map<Integer, ActiveAnimation> activeAnimations = new ConcurrentHashMap<>();
    
    public AnimationManager(Canvas canvas, Consumer<GraphicsContext> baseDrawer) {
        this.canvas = canvas;
        this.baseDrawer = baseDrawer;
    }
    

    public void startAddAnimation(MusicBand band, double finalX, double finalY, double size, Color color, Runnable onComplete) {
        int bandId = band.getId();
        
        double centerX = finalX;
        double centerY = finalY;
        double maxRadius = 80;
        int frames = 50;
        
        Timeline timeline = new Timeline();
        
        for (int i = 0; i <= frames; i++) {
            final double t = (double) i / frames;
            final double angle = t * Math.PI * 4;
            final double radius = maxRadius * (1 - Math.pow(t, 0.7));
            final double currentX = centerX + radius * Math.cos(angle);
            final double currentY = centerY + radius * Math.sin(angle);
            
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(t * 400), e -> {
                updateAnimation(bandId, size, color, currentX, currentY);
                render();
            }));
        }
        
        timeline.setOnFinished(e -> {
            activeAnimations.remove(bandId);
            render();
            if (onComplete != null) onComplete.run();
        });
        
        activeAnimations.put(bandId, new ActiveAnimation(size, color, finalX, finalY));
        timeline.setCycleCount(1);
        timeline.play();
    }
    
 
    private void updateAnimation(int bandId, double size, Color color, double x, double y) {
        ActiveAnimation anim = activeAnimations.get(bandId);
        if (anim != null) {
            anim.size = size;
            anim.color = color;
            anim.x = x;
            anim.y = y;
        } else {
            activeAnimations.put(bandId, new ActiveAnimation(size, color, x, y));
        }
    }
    
    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        baseDrawer.accept(gc);
        
        for (ActiveAnimation anim : activeAnimations.values()) {
            gc.setFill(anim.color);
            gc.fillOval(anim.x, anim.y, anim.size, anim.size);
        }
    }
    
    public boolean isAnimating() {
        return !activeAnimations.isEmpty();
    }
    
    public void stopAllAnimations() {
        activeAnimations.clear();
        render();
    }
    
    private static class ActiveAnimation {
        double size;
        Color color;
        double x;
        double y;
        
        ActiveAnimation(double size, Color color, double x, double y) {
            this.size = size;
            this.color = color;
            this.x = x;
            this.y = y;
        }
    }
}