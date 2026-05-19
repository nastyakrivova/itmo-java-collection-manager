package com.myorg.lab5.client.gui;

import com.myorg.lab5.client.NetworkClient;
import com.myorg.lab5.client.gui.controllers.LoginController;
import com.myorg.lab5.client.gui.controllers.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application{
    private NetworkClient networkClient;
    private String currentLogin;
    private String currentPassword;
    private Stage primaryStage;
    private int currentUserId;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        networkClient = new NetworkClient("localhost", 9807);
        showLoginWindow();
    }

    private void showLoginWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void loginSuccess(String login, String password, int userId) throws Exception {
        this.currentLogin = login;
        this.currentPassword = password;
        this.currentUserId = userId;
        showMainWindow();
    }

    private void showMainWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        
        MainController controller = loader.getController();
        controller.setMainApp(this);
        
        primaryStage.setTitle("MusicBand Manager");
        primaryStage.setScene(new Scene(root));
    }

    public NetworkClient getNetworkClient() { return networkClient; }
    public String getCurrentLogin() { return currentLogin; }
    public String getCurrentPassword() { return currentPassword; }
    public Stage getPrimaryStage() {return primaryStage; }

    public static void main(String[] args) {
        launch(args);
    }
}
