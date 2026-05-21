package com.myorg.lab5.client.gui.utils_gui;

import com.myorg.lab5.client.gui.MainApp;
import com.myorg.lab5.client.gui.controllers.AddDialogController;
import com.myorg.lab5.client.gui.controllers.EditDialogController;
import com.myorg.lab5.model.MusicBand;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;

import java.util.function.Consumer;

public class DialogManager {
    private final MainApp mainApp;
    private final LocalizationManager lang = LocalizationManager.getInstance();
    
    public DialogManager(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void showAddDialog(Consumer<MusicBand> onSave) {
        openBandDialog("/fxml/add_dialog.fxml", lang.get("dialog.add.title"), onSave, null);
    }
    
    public void showAddIfMinDialog(Consumer<MusicBand> onSave) {
        openBandDialog("/fxml/add_dialog.fxml", lang.get("cmd.add_if_min"), onSave, null);
    }
    
    public void showRemoveGreaterDialog(Consumer<MusicBand> onSave) {
        openBandDialog("/fxml/add_dialog.fxml", lang.get("cmd.remove_greater"), onSave, null);
    }
    
    public void showRemoveLowerDialog(Consumer<MusicBand> onSave) {
        openBandDialog("/fxml/add_dialog.fxml", lang.get("cmd.remove_lower"), onSave, null);
    }
    
    public void showEditDialog(MusicBand band, Consumer<MusicBand> onSave) {
        openBandDialog("/fxml/edit_dialog.fxml", lang.get("dialog.edit.title"), onSave, band);
    }
    
    private void openBandDialog(String fxmlPath, String title, Consumer<MusicBand> callback, MusicBand existingBand) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setResources(lang.getBundle());
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainApp.getPrimaryStage());
            stage.setScene(new Scene(root, 500, 450));
            
            if (existingBand != null && fxmlPath.contains("edit_dialog")) {
                EditDialogController controller = loader.getController();
                controller.setDialogStage(stage);
                controller.setBand(existingBand);
                stage.showAndWait();
                if (controller.isSaved()) {
                    callback.accept(controller.getUpdatedBand());
                }
            } else {
                AddDialogController controller = loader.getController();
                controller.setDialogStage(stage);
                stage.showAndWait();
                if (controller.isConfirmed()) {
                    callback.accept(controller.getResultBand());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    
    
    public void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(lang.get("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public boolean showConfirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }
}