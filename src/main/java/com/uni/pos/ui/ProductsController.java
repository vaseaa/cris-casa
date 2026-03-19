package com.uni.pos.ui;

import com.uni.pos.model.Product;
import com.uni.pos.service.ProductService;
import com.uni.pos.util.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProductsController {
  @FXML private TableView<Product> table;
  @FXML private TextField name;
  @FXML private TextField barcode;
  @FXML private TextField categoryId;
  @FXML private TextField price;
  @FXML private TextField vat;
  @FXML private TextField stock;
  @FXML private CheckBox active;

  private ProductService productService;

  public void init(ProductService ps) {
    this.productService = ps;
    table.setItems(productService.getProducts());
  }

  @FXML
  public void save() {
    try {
      Product selected = table.getSelectionModel().getSelectedItem();
      int id = selected == null ? 0 : selected.id();
      Product p = new Product(
          id,
          name.getText(),
          barcode.getText(),
          Integer.parseInt(categoryId.getText()),
          Double.parseDouble(price.getText()),
          Double.parseDouble(vat.getText()),
          Integer.parseInt(stock.getText()),
          active.isSelected()
      );
      productService.save(p);
      productService.reload();
      Dialogs.info("Produs", "Salvat.");
    } catch (Exception e) {
      Dialogs.error("Produs", "Date invalide.");
    }
  }

  @FXML
  public void loadSelected() {
    Product p = table.getSelectionModel().getSelectedItem();
    if (p == null) return;
    name.setText(p.name());
    barcode.setText(p.barcode());
    categoryId.setText(String.valueOf(p.categoryId()));
    price.setText(String.valueOf(p.price()));
    vat.setText(String.valueOf(p.vat()));
    stock.setText(String.valueOf(p.stock()));
    active.setSelected(p.active());
  }
}