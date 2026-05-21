package com.myorg.lab5.client.gui.controllers;

import com.myorg.lab5.client.gui.MainApp;
import com.myorg.lab5.client.gui.drawing.AnimationHelper;
import com.myorg.lab5.client.gui.drawing.AnimationManager;
import com.myorg.lab5.client.gui.drawing.BandDrawer;
import com.myorg.lab5.client.gui.BandService;
import com.myorg.lab5.client.gui.utils_gui.DataParser;
import com.myorg.lab5.client.gui.utils_gui.DialogManager;
import com.myorg.lab5.client.gui.table.TableManager;
import com.myorg.lab5.client.gui.utils_gui.LocalizationManager;
import com.myorg.lab5.model.MusicBand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class MainController {
    
    // UI элементы
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
    @FXML private TextField filterField;
    @FXML private Label userLabel;
    @FXML private Button refreshBtn, addBtn, editBtn, deleteBtn, clearBtn;
    @FXML private Button infoBtn, addIfMinBtn, removeGreaterBtn, removeLowerBtn;
    @FXML private Button countByStudioBtn, executeScriptBtn;
    @FXML private Menu languageMenu;
    
    // Сервисы
    private MainApp mainApp;
    private BandService bandService;
    private DataParser dataParser;
    private DialogManager dialogManager;
    private TableManager tableManager;
    private BandDrawer bandDrawer;
    private AnimationManager animationManager;
    private LocalizationManager lang = LocalizationManager.getInstance();
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.bandService = new BandService(mainApp);
        this.dataParser = new DataParser();
        this.dialogManager = new DialogManager(mainApp);
        
        this.tableManager = new TableManager(bandsTable, idCol, nameCol, participantsCol,
            albumsCol, genreCol, ownerCol, singlesCol, studioCol, creationDateCol, filterField);
        this.bandDrawer = new BandDrawer(canvas, mainApp.getCurrentUserId());

        this.animationManager = new AnimationManager(canvas, gc -> {
            for (MusicBand band : tableManager.getAllData()) {
                drawBand(gc, band);
            }
        });
        
        refreshTexts();
        refreshData();
    }
    
    @FXML
    private void initialize() {
        // Основные команды
        refreshBtn.setOnAction(e -> refreshData());
        addBtn.setOnAction(e -> showAddDialog());
        editBtn.setOnAction(e -> editSelected());
        deleteBtn.setOnAction(e -> deleteSelected());
        clearBtn.setOnAction(e -> clearCollection());
        
        // Дополнительные команды
        infoBtn.setOnAction(e -> showInfo());
        addIfMinBtn.setOnAction(e -> showAddIfMinDialog());
        removeGreaterBtn.setOnAction(e -> showRemoveGreaterDialog());
        removeLowerBtn.setOnAction(e -> showRemoveLowerDialog());
        countByStudioBtn.setOnAction(e -> showCountByStudioDialog());
        executeScriptBtn.setOnAction(e -> executeScript());
        
        canvas.setOnMouseClicked(e -> onCanvasClick(e.getX(), e.getY()));
    }

    private void drawBand(GraphicsContext gc, MusicBand band) {
        double x = band.getCoordinates().getX();
        double y = band.getCoordinates().getY();
        double size = 10 + band.getNumberOfParticipants() / 4;
        Color color = bandDrawer.getColorForOwner(band.getOwnerId());
        
        gc.setFill(color);
        gc.fillOval(x, y, size, size);
        gc.setFill(Color.BLACK);
        gc.fillText(band.getName(), x, y - 5);
    }
    
    public void refreshTexts() {
        // Кнопки
        refreshBtn.setText(lang.get("main.refresh"));
        addBtn.setText(lang.get("cmd.add"));
        editBtn.setText(lang.get("cmd.edit"));
        deleteBtn.setText(lang.get("cmd.delete"));
        clearBtn.setText(lang.get("cmd.clear"));
        infoBtn.setText(lang.get("cmd.info"));
        addIfMinBtn.setText(lang.get("cmd.add_if_min"));
        removeGreaterBtn.setText(lang.get("cmd.remove_greater"));
        removeLowerBtn.setText(lang.get("cmd.remove_lower"));
        countByStudioBtn.setText(lang.get("cmd.count_by_studio"));
        // filterParticipantsBtn.setText(lang.get("cmd.filter"));
        executeScriptBtn.setText(lang.get("cmd.execute_script"));
        
        // Меню
        if (languageMenu != null) languageMenu.setText(lang.get("menu.language"));
        
        // Пользователь с ID
        userLabel.setText(lang.get("main.currentUser") + " " + mainApp.getCurrentLogin() + 
            " (id=" + mainApp.getCurrentUserId() + ")");
        
        // Таблица
        idCol.setText(lang.get("table.id"));
        nameCol.setText(lang.get("table.name"));
        participantsCol.setText(lang.get("table.participants"));
        albumsCol.setText(lang.get("table.albums"));
        genreCol.setText(lang.get("table.genre"));
        ownerCol.setText(lang.get("table.owner"));
        singlesCol.setText(lang.get("table.singles"));
        studioCol.setText(lang.get("table.studio"));
        creationDateCol.setText(lang.get("table.creationDate"));
        filterField.setPromptText(lang.get("main.filter.prompt"));
    }
    
    private void refreshData() {
        new Thread(() -> {
            try {
                List<MusicBand> bands = dataParser.parseShowResponse(bandService.show());
                Platform.runLater(() -> {
                    tableManager.setData(bands);
                    // bandDrawer.draw(bands);
                    animationManager.render();
                    // if (onComplete != null) {
                    //     onComplete.run();
                    // }
                });
            } catch (Exception e) {
                Platform.runLater(() -> dialogManager.showError(e.getMessage()));
            }
        }).start();
    }
    
    private void showInfo() {
        new Thread(() -> {
            try {
                String info = dataParser.parseInfoResponse(bandService.info());
                Platform.runLater(() -> dialogManager.showInfo(lang.get("cmd.info"), info));
            } catch (Exception e) {
                Platform.runLater(() -> dialogManager.showError(e.getMessage()));
            }
        }).start();
    }
    
    // !!!!!!!
    // private void refreshData() {
    //     refreshData(null);
    // }

    // private void showAddDialog() {
    //     dialogManager.showAddDialog(band -> {
    //         new Thread(() -> {
    //             try {
    //                 bandService.add(band);
    //                 Platform.runLater(() -> {
    //                     refreshData(() -> {
    //                         MusicBand addedBand = findBandByName(band.getName());
    //                         if (addedBand != null) {
    //                             double x = addedBand.getCoordinates().getX();
    //                             double y = addedBand.getCoordinates().getY();
    //                             double size = 10 + addedBand.getNumberOfParticipants() / 4;
    //                             Color color = bandDrawer.getColorForOwner(mainApp.getCurrentUserId());
    //                             animationManager.startAddAnimation(addedBand, x, y, size, color, null);
    //                         }
    //                     });
    //                 });
    //             } catch (Exception e) {
    //                 Platform.runLater(() -> dialogManager.showError(e.getMessage()));
    //             }
    //         }).start();
    //     });
    // }


    // private void showAddDialog() {
    //     dialogManager.showAddDialog(band -> {
    //         new Thread(() -> {
    //             try {
    //                 bandService.add(band);
    //                 Platform.runLater(() -> {
    //                     refreshData();
    //                     MusicBand addedBand = findBandByName(band.getName());
    //                     if (addedBand != null) {
    //                         double x = addedBand.getCoordinates().getX();
    //                         double y = addedBand.getCoordinates().getY();
    //                         double size = 10 + addedBand.getNumberOfParticipants() / 4;
    //                         Color color = bandDrawer.getColorForOwner(mainApp.getCurrentUserId());
                            
    //                         animationManager.startAddAnimation(addedBand, x, y, size, color, null);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 Platform.runLater(() -> dialogManager.showError(e.getMessage()));
    //             }
    //         }).start();
    //     });
    // }

    private void showAddDialog() {
        dialogManager.showAddDialog(band -> {
            new Thread(() -> {
                try {
                    bandService.add(band);
                    Platform.runLater(() -> {
                        // Анимация сразу, без ожидания refreshData
                        double x = band.getCoordinates().getX();
                        double y = band.getCoordinates().getY();
                        double size = 10 + band.getNumberOfParticipants() / 4;
                        Color color = bandDrawer.getColorForOwner(mainApp.getCurrentUserId());
                        
                        // Создаём временный объект для анимации
                        MusicBand tempBand = new MusicBand(
                            band.getName(),
                            band.getCoordinates(),
                            band.getNumberOfParticipants(),
                            band.getAlbumsCount(),
                            band.getGenre(),
                            band.getStudio(),
                            band.getSinglesCount()
                        );
                        tempBand.setId(-1);
                        tempBand.setCoordinates(band.getCoordinates());
                        tempBand.setNumberOfParticipants(band.getNumberOfParticipants());
                        tempBand.setName(band.getName());
                        tempBand.setOwnerId(mainApp.getCurrentUserId());
                        
                        animationManager.startAddAnimation(tempBand, x, y, size, color, () -> {
                            refreshData();  // после анимации обновляем данные
                        });
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        });
    }

    // private void showAddDialog() {
    //     dialogManager.showAddDialog(band -> {
    //         new Thread(() -> {
    //             try {
    //                 bandService.add(band);
    //                 Thread.sleep(500);  // ждём полсекунды, чтобы сервер успел обработать
    //                 Platform.runLater(() -> {
    //                     refreshData();
    //                     // Даём время на отрисовку
    //                     Platform.runLater(() -> {
    //                         MusicBand addedBand = findBandByName(band.getName());
    //                         if (addedBand != null) {
    //                             double x = addedBand.getCoordinates().getX();
    //                             double y = addedBand.getCoordinates().getY();
    //                             double size = 10 + addedBand.getNumberOfParticipants() / 4;
    //                             Color color = bandDrawer.getColorForOwner(mainApp.getCurrentUserId());
    //                             animationManager.startAddAnimation(addedBand, x, y, size, color, null);
    //                         }
    //                     });
    //                 });
    //             } catch (Exception e) {
    //                 Platform.runLater(() -> dialogManager.showError(e.getMessage()));
    //             }
    //         }).start();
    //     });
    // }
    
    private void showAddIfMinDialog() {
        dialogManager.showAddIfMinDialog(band -> {
            new Thread(() -> {
                try {
                    bandService.addIfMin(band);
                    Platform.runLater(() -> refreshData());
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        });
    }
    
    private void editSelected() {
        MusicBand selected = tableManager.getSelected();
        if (selected == null) {
            dialogManager.showError(lang.get("error.noSelection"));
            return;
        }
        if (selected.getOwnerId() != mainApp.getCurrentUserId()) {
            dialogManager.showError(lang.get("error.notOwner"));
            return;
        }
        dialogManager.showEditDialog(selected, updatedBand -> {
            new Thread(() -> {
                try {
                    bandService.update(selected.getId(), updatedBand);
                    Platform.runLater(() -> refreshData());
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        });
    }
    
    private void deleteSelected() {
        MusicBand selected = tableManager.getSelected();
        if (selected == null) {
            dialogManager.showError(lang.get("error.noSelection"));
            return;
        }

        if (selected.getOwnerId() != mainApp.getCurrentUserId()) {
            dialogManager.showError(lang.get("error.notOwner"));
            return;
        }

        if (dialogManager.showConfirmDialog(lang.get("cmd.delete"), 
                "Удалить \"" + selected.getName() + "\"?")) {
            new Thread(() -> {
                try {
                    bandService.removeById(selected.getId());
                    Platform.runLater(() -> refreshData());
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        }
    }
    
    private void showRemoveGreaterDialog() {
        dialogManager.showRemoveGreaterDialog(band -> {
            new Thread(() -> {
                try {
                    bandService.removeGreater(band);
                    Platform.runLater(() -> refreshData());
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        });
    }
    
    private void showRemoveLowerDialog() {
        dialogManager.showRemoveLowerDialog(band -> {
            new Thread(() -> {
                try {
                    bandService.removeLower(band);
                    Platform.runLater(() -> refreshData());
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        });
    }
    
    private void showCountByStudioDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(lang.get("cmd.count_by_studio"));
        dialog.setHeaderText(lang.get("dialog.studio.name"));
        dialog.setContentText(lang.get("dialog.studio.name"));
        dialog.showAndWait().ifPresent(studioName -> {
            if (!studioName.trim().isEmpty()) {
                new Thread(() -> {
                    try {
                        int count = dataParser.parseCountResponse(bandService.countByStudio(studioName.trim()));
                        Platform.runLater(() -> dialogManager.showInfo(lang.get("cmd.count_by_studio"),
                            java.text.MessageFormat.format(lang.get("msg.studioCount"), studioName, count)));
                    } catch (Exception e) {
                        Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                    }
                }).start();
            }
        });
    }
    
    private void clearCollection() {
        if (dialogManager.showConfirmDialog(lang.get("cmd.clear"), lang.get("cmd.clear") + "?")) {
            new Thread(() -> {
                try {
                    bandService.clear();
                    Platform.runLater(() -> refreshData());
                } catch (Exception e) {
                    Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                }
            }).start();
        }
    }
    
    private void executeScript() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(lang.get("cmd.execute_script"));
        dialog.setHeaderText("execute_script");
        dialog.setContentText("File name:");
        dialog.showAndWait().ifPresent(fileName -> {
            if (!fileName.trim().isEmpty()) {
                // TODO: Реализовать выполнение скрипта
                dialogManager.showInfo(lang.get("cmd.execute_script"), "Executing script: " + fileName);
            }
        });
    }
    
    private void onCanvasClick(double x, double y) {
        MusicBand band = bandDrawer.findBandAt(tableManager.getAllData(), x, y);
        if (band != null) {
            boolean isOwner = band.getOwnerId() == mainApp.getCurrentUserId();
            String info = String.format("ID: %d\n%s: %s\n%s: %d\n%s: %d\n%s: %s\n%s: %s\n%s: %d",
                band.getId(),
                lang.get("table.name"), band.getName(),
                lang.get("table.participants"), band.getNumberOfParticipants(),
                lang.get("table.albums"), band.getAlbumsCount(),
                lang.get("table.genre"), band.getGenre(),
                lang.get("table.studio"), band.getStudio() != null ? band.getStudio().getName() : "-",
                lang.get("table.owner"), band.getOwnerId());
            
            if (isOwner) {
                showBandInfoWithEditButton(band, info);
                // dialogManager.showEditDialog(band, updatedBand -> {
                //     new Thread(() -> {
                //         try {
                //             bandService.update(band.getId(), updatedBand);
                //             Platform.runLater(() -> refreshData());
                //         } catch (Exception e) {
                //             Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                //         }
                //     }).start();
                // });
            } else {
                dialogManager.showInfo(lang.get("cmd.info"), info);
            }
        }
    }

    private void showBandInfoWithEditButton(MusicBand band, String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("cmd.info"));
        alert.setHeaderText(band.getName());
        alert.setContentText(info);
        
        ButtonType editButton = new ButtonType(lang.get("cmd.edit"));
        alert.getButtonTypes().setAll(editButton, ButtonType.CLOSE);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == editButton) {
                // Открываем диалог редактирования
                dialogManager.showEditDialog(band, updatedBand -> {
                    new Thread(() -> {
                        try {
                            bandService.update(band.getId(), updatedBand);
                            Platform.runLater(() -> refreshData());
                        } catch (Exception e) {
                            Platform.runLater(() -> dialogManager.showError(e.getMessage()));
                        }
                    }).start();
                });
            }
        });
    }
    
    // Переключение языков
    public void setRussian() { changeLanguage(new Locale("ru")); }
    public void setEnglish() { changeLanguage(new Locale("en")); }
    public void setGerman() { changeLanguage(new Locale("de")); }
    public void setItalian() { changeLanguage(new Locale("it")); }
    
    private void changeLanguage(Locale locale) {
        lang.changeLocale(locale);
        refreshTexts();
        dialogManager.showInfo(lang.get("menu.language"), "Language changed to " + locale.getDisplayName());
    }

    private MusicBand findBandByName(String name) {
        return tableManager.getAllData().stream()
            .filter(b -> b.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
}