package com.myorg.lab5.client.gui.controllers;

import com.myorg.lab5.client.gui.MainApp;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.MusicGenre;
import com.myorg.lab5.model.Studio;
import com.myorg.lab5.utils.MusicBandParser;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    
    @FXML private TableView<MusicBand> bandsTable;
    @FXML private TableColumn<MusicBand, Integer> idCol;
    @FXML private TableColumn<MusicBand, String> nameCol;
    @FXML private TableColumn<MusicBand, Integer> participantsCol;
    @FXML private TableColumn<MusicBand, Integer> albumsCol;
    @FXML private TableColumn<MusicBand, String> genreCol;
    @FXML private TableColumn<MusicBand, Integer> ownerCol;
    @FXML private TableColumn<MusicBand, Integer> singlesCol;
    @FXML private TableColumn<MusicBand, String> studioCol;
    @FXML private TableColumn<MusicBand, LocalDate> creationDateCol;
    
    @FXML private Canvas canvas;
    
    @FXML private Button refreshBtn;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button clearBtn;
    
    @FXML private TextField filterField;
    @FXML private Label userLabel;
    
    private MainApp mainApp;
    private ObservableList<MusicBand> bandData = FXCollections.observableArrayList();
    private FilteredList<MusicBand> filteredData;
    private SortedList<MusicBand> sortedData;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        userLabel.setText(mainApp.getCurrentLogin() + " (id=" + mainApp.getCurrentUserId() + ")");
    }
    
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        participantsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albumsCount"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        singlesCol.setCellValueFactory(new PropertyValueFactory<>("singlesCount"));
        studioCol.setCellValueFactory(new PropertyValueFactory<>("studio"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        studioCol.setCellValueFactory(cellData -> {
            Studio studio = cellData.getValue().getStudio();
            return new SimpleStringProperty(studio != null ? studio.getName() : "");
        });
                        
        // фильтрация
        filteredData = new FilteredList<>(bandData, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bandsTable.comparatorProperty());
        bandsTable.setItems(sortedData);
        
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(band -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerFilter = newValue.toLowerCase();
                return band.getName().toLowerCase().contains(lowerFilter) ||
                       String.valueOf(band.getId()).contains(lowerFilter);
            });
        });
        
        refreshBtn.setOnAction(e -> refreshData());
        addBtn.setOnAction(e -> showAddDialog());
        editBtn.setOnAction(e -> editSelectedBand());
        deleteBtn.setOnAction(e -> deleteSelectedBand());
        clearBtn.setOnAction(e -> clearCollection());
        
        canvas.setOnMouseClicked(e -> onCanvasClick(e.getX(), e.getY()));
        
        refreshData();
    }
    
    private void refreshData() {
        new Thread(() -> {
            try {
                CommandRequest request = new CommandRequest("show", 
                    mainApp.getCurrentLogin(), mainApp.getCurrentPassword());
                CommandResponse response = mainApp.getNetworkClient().sendCommand(request);
                
                if (response.isSuccess()) {
                    List<MusicBand> bands = parseBandsFromResponse(response.getMessage());
                    Platform.runLater(() -> {
                        bandData.setAll(bands);
                        drawBands();
                    });
                } else {
                    Platform.runLater(() -> showError("Ошибка", response.getMessage()));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showError("Ошибка", e.getMessage()));
            }
        }).start();
    }
    
    private void drawBands() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (MusicBand band : bandData) {
            double x = band.getCoordinates().getX();
            double y = band.getCoordinates().getY();
            double size = 10 + band.getNumberOfParticipants() / 4;
            Color color = getColorForOwner(band.getOwnerId());
            
            gc.setFill(color);
            gc.fillOval(x, y, size, size);             
            gc.setFill(Color.BLACK);
            gc.fillText(band.getName(), x, y - 5);
        }
    }
    
    private Color getColorForOwner(int ownerId) {
        if (ownerId == mainApp.getCurrentUserId()) {
            return Color.LIGHTGREEN;
        }
        double hue = (ownerId * 137) % 360;
        double saturation = 0.6;
        double brightness = 0.9;
        
        return Color.hsb(hue, saturation, brightness);
    }
    
    // adding
    private void showAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_dialog.fxml"));
            Parent root = loader.load();
            AddDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавление группы");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(root, 500, 400));
            
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
            if (controller.isConfirmed()) {
                MusicBand newBand = controller.getResultBand();
                sendAddCommand(newBand);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ошибка", "Не удалось открыть диалог добавления");
        }
    }

    private void sendAddCommand(MusicBand band) {
        new Thread(() -> {
            try {
                MusicBandParser parser = new MusicBandParser();
                String bandData = parser.toCsv(band);
                
                CommandRequest request = new CommandRequest("add", 
                    new Object[]{bandData}, 
                    mainApp.getCurrentLogin(), 
                    mainApp.getCurrentPassword());
                
                CommandResponse response = mainApp.getNetworkClient().sendCommand(request);
                
                Platform.runLater(() -> {
                    if (response.isSuccess()) {
                        refreshData();
                    } else {
                        showError("Ошибка", response.getMessage());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Ошибка", e.getMessage()));
            }
        }).start();
    }

    // editing
    private void editSelectedBand() {
        MusicBand selected = bandsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Ошибка", "Выберите объект для редактирования");
            return;
        }
        if (selected.getOwnerId() != mainApp.getCurrentUserId()) {
            showError("Ошибка", "Вы можете редактировать только свои объекты");
            return;
        }
        showEditDialogForBand(selected);
    }

    private void showEditDialogForBand(MusicBand band) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_dialog.fxml"));
            Parent root = loader.load();
            
            EditDialogController controller = loader.getController();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование группы");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(root, 500, 450));
            dialogStage.setResizable(false);
            
            controller.setDialogStage(dialogStage);
            controller.setBand(band);
            
            dialogStage.showAndWait();
            
            if (controller.isSaved()) {
                sendUpdateCommand(band.getId(), controller.getUpdatedBand());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ошибка", "Не удалось открыть диалог редактирования: " + e.getMessage());
        }
    }

    private void sendUpdateCommand(int id, MusicBand updatedBand) {
        new Thread(() -> {
            try {
                MusicBandParser parser = new MusicBandParser();
                String bandData = parser.toCsv(updatedBand);
                
                CommandRequest request = new CommandRequest("update", 
                    new Object[]{id, bandData}, 
                    mainApp.getCurrentLogin(), 
                    mainApp.getCurrentPassword());
                
                CommandResponse response = mainApp.getNetworkClient().sendCommand(request);
                
                Platform.runLater(() -> {
                    if (response.isSuccess()) {
                        refreshData();
                    } else {
                        showError("Ошибка", response.getMessage());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Ошибка", e.getMessage()));
            }
        }).start();
    }
    
    private void deleteSelectedBand() {
        MusicBand selected = bandsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Ошибка", "Выберите объект для удаления");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setContentText("Удалить группу \"" + selected.getName() + "\"?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        CommandRequest request = new CommandRequest("remove_by_id", 
                            new Object[]{selected.getId()}, 
                            mainApp.getCurrentLogin(), 
                            mainApp.getCurrentPassword());
                        CommandResponse resp = mainApp.getNetworkClient().sendCommand(request);
                        Platform.runLater(() -> {
                            if (resp.isSuccess()) {
                                refreshData();
                            } else {
                                showError("Ошибка", resp.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> showError("Ошибка", e.getMessage()));
                    }
                }).start();
            }
        });
    }
    
    private void clearCollection() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setContentText("Очистить всю коллекцию?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        CommandRequest request = new CommandRequest("clear", 
                            mainApp.getCurrentLogin(), mainApp.getCurrentPassword());
                        CommandResponse resp = mainApp.getNetworkClient().sendCommand(request);
                        Platform.runLater(() -> {
                            if (resp.isSuccess()) {
                                refreshData();
                            } else {
                                showError("Ошибка", resp.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> showError("Ошибка", e.getMessage()));
                    }
                }).start();
            }
        });
    }
    
    private void onCanvasClick(double x, double y) {
        for (MusicBand band : bandData) {
            double bandX = band.getCoordinates().getX();
            double bandY = band.getCoordinates().getY();
            double size = 30 + band.getNumberOfParticipants() / 2;
            
            if (x >= bandX && x <= bandX + size && y >= bandY && y <= bandY + size) {
                showBandInfo(band);
                break;
            }
        }
    }
    
    private void showBandInfo(MusicBand band) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Информация о группе");
        info.setHeaderText(band.getName());
        info.setContentText(
            "ID: " + band.getId() + "\n" +
            "Участников: " + band.getNumberOfParticipants() + "\n" +
            "Альбомов: " + band.getAlbumsCount() + "\n" +
            "Жанр: " + band.getGenre() + "\n" +
            "Студия: " + (band.getStudio() != null ? band.getStudio().getName() : "не указана") + "\n" +
            "Владелец: " + band.getOwnerId()
        );
        info.showAndWait();
    }
    
    private List<MusicBand> parseBandsFromResponse(String message) {
        List<MusicBand> bands = new ArrayList<>();
    
        if (message == null || message.trim().isEmpty()) {
            return bands;
        }
        
        MusicBandParser parser = new MusicBandParser();
        String[] lines = message.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            try {
                MusicBand band = parser.parseFromString(line);
                bands.add(band);
            } catch (Exception e) {
                System.err.println("Ошибка парсинга: " + line);
                e.printStackTrace();
            }
        }
        
        System.out.println("Parsed " + bands.size() + " bands");
        return bands;
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}