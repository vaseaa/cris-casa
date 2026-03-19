package com.uni.pos.ui;

import com.uni.pos.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class ReportsController {
  @FXML private TextArea output;

  private ReportService reportService;

  public void init(ReportService rs) {
    this.reportService = rs;
  }

  @FXML
  public void loadSales() {
    output.setText(reportService.salesSummary());
  }

  @FXML
  public void loadTopProducts() {
    output.setText(reportService.topProducts());
  }

  @FXML
  public void export() {
    FileChooser fc = new FileChooser();
    fc.setInitialFileName("raport.txt");
    var f = fc.showSaveDialog(output.getScene().getWindow());
    if (f != null) reportService.exportToFile(output.getText(), f.getAbsolutePath());
  }
}