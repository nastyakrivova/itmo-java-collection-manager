package com.myorg.lab5.client.gui.drawing;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AnimationHelper {
    
    public static void playAddAnimation(Canvas canvas, double finalX, double finalY, double size, Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        javafx.scene.image.WritableImage snapshot = canvas.snapshot(null, null);
        
        // Начальная точка (слева от конечной)
        double startX = finalX - 150;
        double startY = finalY;
        
        // Параметры спирали
        int frames = 50;  // количество кадров
        double centerX = finalX;
        double centerY = finalY;
        double maxRadius = 80;  // максимальный радиус спирали
        
        Timeline timeline = new Timeline();
        
        for (int i = 0; i <= frames; i++) {
            final int step = i;
            final double t = (double) step / frames;  // прогресс 0..1
            
            // Спираль: угол растёт, радиус уменьшается к концу
            double angle = t * Math.PI * 4;  // 2 полных оборота
            double radius = maxRadius * (1 - t);  // радиус уменьшается к 0
            
            // Текущие координаты на спирали
            double currentX = centerX + radius * Math.cos(angle);
            double currentY = centerY + radius * Math.sin(angle);
            
            KeyFrame frame = new KeyFrame(Duration.millis(t * 500), event -> {
                // Очищаем область вокруг движущегося шарика
                // double animRadius = size + 20;
                // gc.clearRect(currentX - animRadius, currentY - animRadius, 
                //              animRadius * 2, animRadius * 2);

                gc.drawImage(snapshot, 0, 0);
                
                // Рисуем шарик
                gc.setFill(color);
                gc.fillOval(currentX, currentY, size, size);
            });
            
            timeline.getKeyFrames().add(frame);
        }
        
        timeline.setCycleCount(1);
        timeline.play();
    }
    
    public static void playDeleteAnimation(Canvas canvas, double x, double y, double size) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                gc.setFill(Color.RED);
                gc.fillOval(x, y, size, size);
            }),
            new KeyFrame(Duration.millis(100), e -> {
                gc.fillOval(x + 2, y + 2, size - 4, size - 4);
            }),
            new KeyFrame(Duration.millis(200), e -> {
                gc.fillOval(x + 4, y + 4, size - 8, size - 8);
            }),
            new KeyFrame(Duration.millis(300), e -> {
                gc.fillOval(x + 6, y + 6, size - 12, size - 12);
            }),
            new KeyFrame(Duration.millis(400), e -> {
                gc.fillOval(x + 8, y + 8, size - 16, size - 16);
            }),
            new KeyFrame(Duration.millis(500), e -> {
                gc.clearRect(x, y, size, size);
            })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
