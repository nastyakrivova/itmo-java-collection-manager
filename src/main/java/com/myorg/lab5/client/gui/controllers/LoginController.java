package com.myorg.lab5.client.gui.controllers;

import com.myorg.lab5.client.gui.utils_gui.LocalizationManager;

import java.util.Locale;

import com.myorg.lab5.client.gui.MainApp;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    private MainApp mainApp;
    private LocalizationManager lang = LocalizationManager.getInstance();


    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        titleLabel.setText(lang.get("login.title"));
        loginBtn.setText(lang.get("login.loginBtn"));
        registerBtn.setText(lang.get("login.registerBtn"));

        loginBtn.setOnAction(e -> authenticate("login"));
        registerBtn.setOnAction(e -> authenticate("register"));
    }

    private void authenticate(String command){
        String login = loginField.getText();
        String password = passwordField.getText();

        loginBtn.setDisable(true);
        registerBtn.setDisable(true);

        new Thread(() -> {
            try{
                CommandRequest request = new CommandRequest(command, new Object[]{login, password}, null, null);
                CommandResponse response = mainApp.getNetworkClient().sendCommand(request);
                Platform.runLater(() -> {
                    if (response.isSuccess()){
                        int userId = extractUserId(response.getMessage());
                        try {
                            // int userId = extractUserId(response.getMessage());
                            // mainApp.loginSuccess(login, passord, userId);
                            mainApp.loginSuccess(login, password, userId);
                        } catch (Exception e) {
                            showError("Ошибка", e.getMessage());
                        }
                    } else {
                        showError("Ошибка авторизации", response.getMessage());
                    }
                    loginBtn.setDisable(false);
                    registerBtn.setDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Ошибка", e.getMessage());
                    loginBtn.setDisable(false);
                    registerBtn.setDisable(false);
                });
            }
        }).start();
    }

    private int extractUserId(String message) {
        if (message.contains("UserId:")) {
            String[] parts = message.split("UserId:");
            try {
                return Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse userId from: " + message);
            }
        }
        return -1;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setRussian() { changeLanguage(new Locale("ru")); }
    public void setEnglish() { changeLanguage(new Locale("en")); }
    public void setGerman() { changeLanguage(new Locale("de")); }
    public void setItalian() { changeLanguage(new Locale("it")); }

    private void changeLanguage(Locale locale) {
        lang.changeLocale(locale);
        refreshTexts();
    }

    public void refreshTexts() {
        titleLabel.setText(lang.get("login.title"));
        usernameLabel.setText(lang.get("login.username"));
        passwordLabel.setText(lang.get("login.password"));
        loginBtn.setText(lang.get("login.loginBtn"));
        registerBtn.setText(lang.get("login.registerBtn"));
    }
}
