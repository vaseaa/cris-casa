package com.uni.pos.ui;

import com.uni.pos.service.SettingsService;
import com.uni.pos.util.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsController {
  @FXML private TextField storeName;
  @FXML private TextField currency;

  private SettingsService settingsService;

  public void init(SettingsService ss) {
    this.settingsService = ss;
    storeName.setText(ss.get("store_name", "POS"));
    currency.setText(ss.get("currency", "RON"));
  }

  @FXML
  public void save() {
    settingsService.set("store_name", storeName.getText());
    settingsService.set("currency", currency.getText());
    Dialogs.info("Setari", "Salvate.");
  }
}