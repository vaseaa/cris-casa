package com.uni.pos;

import com.uni.pos.db.Schema;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Schema.init();

    FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/LoginView.fxml"));
    Scene scene = new Scene(loader.load(), 1100, 720);
    scene.getStylesheets().add(MainApp.class.getResource("/css/styles.css").toExternalForm());

    stage.setTitle("POS Simulat - Universitate");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}