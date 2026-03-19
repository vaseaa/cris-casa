package com.uni.pos.ui;

import com.uni.pos.service.CartService;
import com.uni.pos.service.CustomerService;
import com.uni.pos.service.ProductService;
import com.uni.pos.service.ReceiptService;
import com.uni.pos.service.ReportService;
import com.uni.pos.service.SettingsService;
import javafx.fxml.FXML;

public class MainController {
  private final ProductService productService = new ProductService();
  private final CustomerService customerService = new CustomerService();
  private final CartService cartService = new CartService();
  private final ReceiptService receiptService = new ReceiptService(productService);
  private final ReportService reportService = new ReportService();
  private final SettingsService settingsService = new SettingsService();

  @FXML private ProductsController productsController;
  @FXML private CartController cartController;
  @FXML private CustomersController customersController;
  @FXML private ReportsController reportsController;
  @FXML private SettingsController settingsController;

  @FXML
  public void initialize() {
    productsController.init(productService);
    cartController.init(productService, customerService, cartService, receiptService, settingsService);
    customersController.init(customerService);
    reportsController.init(reportService);
    settingsController.init(settingsService);
  }
}