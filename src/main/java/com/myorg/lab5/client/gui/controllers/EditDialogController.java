package com.myorg.lab5.client.gui.controllers;

import com.myorg.lab5.model.Coordinates;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditDialogController {
    
    @FXML private Label idLabel;
    @FXML private TextField nameField;
    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private TextField participantsField;
    @FXML private TextField albumsField;
    @FXML private TextField singlesField;
    @FXML private ComboBox<MusicGenre> genreCombo;
    @FXML private TextField studioField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    
    private Stage dialogStage;
    private MusicBand originalBand;
    private MusicBand updatedBand;
    private boolean saved = false;
    
    @FXML
    private void initialize() {
        genreCombo.getItems().setAll(MusicGenre.values());
        
        saveBtn.setOnAction(e -> saveChanges());
        cancelBtn.setOnAction(e -> {
            saved = false;
            dialogStage.close();
        });
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setBand(MusicBand band) {
        this.originalBand = band;
        
        idLabel.setText(String.valueOf(band.getId()));
        nameField.setText(band.getName());
        xField.setText(String.valueOf(band.getCoordinates().getX()));
        yField.setText(String.valueOf(band.getCoordinates().getY()));
        participantsField.setText(String.valueOf(band.getNumberOfParticipants()));
        albumsField.setText(String.valueOf(band.getAlbumsCount()));
        
        if (band.getSinglesCount() != null) {
            singlesField.setText(String.valueOf(band.getSinglesCount()));
        }
        
        genreCombo.setValue(band.getGenre());
        
        if (band.getStudio() != null) {
            studioField.setText(band.getStudio().getName());
        }
    }
    
    private void saveChanges() {
        try {
            // Валидация полей
            int id = originalBand.getId();
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Название не может быть пустым");
                return;
            }
            
            int x = Integer.parseInt(xField.getText().trim());
            if (x > 290) {
                showError("X не может быть больше 290");
                return;
            }
            
            int y = Integer.parseInt(yField.getText().trim());
            Coordinates coordinates = new Coordinates(x, y);
            
            int participants = Integer.parseInt(participantsField.getText().trim());
            if (participants <= 0) {
                showError("Участников должно быть больше 0");
                return;
            }
            
            int albums = Integer.parseInt(albumsField.getText().trim());
            if (albums <= 0) {
                showError("Альбомов должно быть больше 0");
                return;
            }
            
            Integer singles = null;
            if (!singlesField.getText().trim().isEmpty()) {
                singles = Integer.parseInt(singlesField.getText().trim());
                if (singles <= 0) {
                    showError("Синглов должно быть больше 0");
                    return;
                }
            }
            
            MusicGenre genre = genreCombo.getValue();
            
            Studio studio = null;
            if (!studioField.getText().trim().isEmpty()) {
                studio = new Studio(studioField.getText().trim());
            }
            
            updatedBand = new MusicBand(name, coordinates, participants, albums, genre, studio, singles);
            updatedBand.setId(originalBand.getId());
            updatedBand.setOwnerId(originalBand.getOwnerId());
            
            saved = true;
            dialogStage.close();
            
        } catch (NumberFormatException e) {
            showError("Введите корректные числа");
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    public MusicBand getUpdatedBand() {
        return updatedBand;
    }
}