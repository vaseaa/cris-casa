package com.uni.pos.ui;

import com.uni.pos.model.Customer;
import com.uni.pos.service.CustomerService;
import com.uni.pos.util.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CustomersController {
  @FXML private TableView<Customer> table;
  @FXML private TextField name;
  @FXML private TextField phone;
  @FXML private TextField email;
  @FXML private TextField points;

  private CustomerService customerService;

  public void init(CustomerService cs) {
    this.customerService = cs;
    table.setItems(customerService.getCustomers());
  }

  @FXML
  public void save() {
    try {
      Customer selected = table.getSelectionModel().getSelectedItem();
      int id = selected == null ? 0 : selected.id();
      Customer c = new Customer(
          id, name.getText(), phone.getText(), email.getText(),
          Integer.parseInt(points.getText())
      );
      customerService.save(c);
      customerService.reload();
      Dialogs.info("Client", "Salvat.");
    } catch (Exception e) {
      Dialogs.error("Client", "Date invalide.");
    }
  }

  @FXML
  public void loadSelected() {
    Customer c = table.getSelectionModel().getSelectedItem();
    if (c == null) return;
    name.setText(c.name());
    phone.setText(c.phone());
    email.setText(c.email());
    points.setText(String.valueOf(c.loyaltyPoints()));
  }
}