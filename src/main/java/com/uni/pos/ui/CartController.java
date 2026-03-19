package com.uni.pos.ui;

import com.uni.pos.model.CartItem;
import com.uni.pos.model.Customer;
import com.uni.pos.model.Product;
import com.uni.pos.model.Receipt;
import com.uni.pos.service.*;
import com.uni.pos.util.Dialogs;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CartController {
  @FXML private TextField barcode;
  @FXML private TableView<CartItem> cartTable;
  @FXML private Label total;
  @FXML private Label vat;
  @FXML private Label discount;
  @FXML private ComboBox<Customer> customer;
  @FXML private ComboBox<String> payment;
  @FXML private TextField cash;

  private ProductService productService;
  private CustomerService customerService;
  private CartService cartService;
  private ReceiptService receiptService;
  private SettingsService settingsService;

  public void init(ProductService ps, CustomerService cs, CartService cart, ReceiptService rs, SettingsService ss) {
    this.productService = ps;
    this.customerService = cs;
    this.cartService = cart;
    this.receiptService = rs;
    this.settingsService = ss;

    cartTable.setItems(cartService.getItems());
    cartService.getItems().addListener((ListChangeListener<CartItem>) c -> recalc());
    customer.setItems(customerService.getCustomers());
    payment.getItems().addAll("Cash", "Card");
    payment.getSelectionModel().selectFirst();
  }

  @FXML
  public void addByBarcode() {
    Product p = productService.findByBarcode(barcode.getText());
    if (p == null) {
      Dialogs.error("Cos", "Produs inexistent.");
      return;
    }
    if (p.stock() <= 0) {
      Dialogs.error("Cos", "Stoc insuficient.");
      return;
    }
    cartService.addProduct(p);
    cartTable.refresh();
    barcode.clear();
    recalc();
  }

  @FXML
  public void applyDiscount() {
    CartItem it = cartTable.getSelectionModel().getSelectedItem();
    if (it == null) return;
    TextInputDialog d = new TextInputDialog("0");
    d.setHeaderText("Reducere pe linie (valoare absoluta)");
    d.showAndWait().ifPresent(v -> {
      try {
        it.setDiscount(Double.parseDouble(v));
        cartTable.refresh();
        recalc();
      } catch (Exception ignored) {}
    });
  }

  @FXML
  public void removeSelected() {
    CartItem it = cartTable.getSelectionModel().getSelectedItem();
    if (it != null) cartService.remove(it);
    recalc();
  }

  @FXML
  public void finalizeSale() {
    if (cartService.getItems().isEmpty()) return;

    Customer c = customer.getSelectionModel().getSelectedItem();
    cartService.setCustomer(c);

    double cashVal = 0;
    if (payment.getValue().equals("Cash")) {
      try { cashVal = Double.parseDouble(cash.getText()); }
      catch (Exception e) { Dialogs.error("Plata", "Cash invalid."); return; }
    }

    Receipt r = receiptService.finalizeReceipt(
        cartService.getItems(), c, payment.getValue(), cashVal);

    if (r != null && c != null) {
      int points = (int)Math.floor(r.total());
      customerService.addPoints(c.id(), points);
    }

    cartService.clear();
    cartTable.refresh();
    recalc();
    Dialogs.info("Bon", "Tranzactie finalizata.");
  }

  private void recalc() {
    total.setText(String.format("%.2f %s", cartService.total(), settingsService.get("currency","RON")));
    vat.setText(String.format("%.2f", cartService.vatTotal()));
    discount.setText(String.format("%.2f", cartService.discountTotal()));
  }
}