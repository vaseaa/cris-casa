package com.uni.pos.ui;

import com.uni.pos.MainApp;
import com.uni.pos.service.UserService;
import com.uni.pos.util.Dialogs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
  @FXML private TextField username;
  @FXML private PasswordField password;

  private final UserService userService = new UserService();

  @FXML
  public void login() {
    if (userService.authenticate(username.getText(), password.getText())) {
      try {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 760);
        scene.getStylesheets().add(MainApp.class.getResource("/css/styles.css").toExternalForm());

        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(scene);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Dialogs.error("Autentificare", "Date invalide.");
    }
  }
}