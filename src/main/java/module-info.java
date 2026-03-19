module com.uni.pos {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  opens com.uni.pos to javafx.graphics;
  opens com.uni.pos.ui to javafx.fxml;

  exports com.uni.pos;
  exports com.uni.pos.ui;
  exports com.uni.pos.model;
}