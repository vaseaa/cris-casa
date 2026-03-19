package com.uni.pos.service;

import com.uni.pos.model.CartItem;
import com.uni.pos.model.Customer;
import com.uni.pos.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartService {
  private final ObservableList<CartItem> items = FXCollections.observableArrayList();
  private Customer customer;

  public ObservableList<CartItem> getItems() { return items; }

  public void addProduct(Product p) {
    for (CartItem i : items) {
      if (i.getProduct().id() == p.id()) {
        i.setQty(i.getQty() + 1);
        return;
      }
    }
    items.add(new CartItem(p, 1));
  }

  public void remove(CartItem item) { items.remove(item); }

  public void clear() { items.clear(); customer = null; }

  public double total() {
    return items.stream().mapToDouble(CartItem::lineTotal).sum();
  }

  public double vatTotal() {
    return items.stream().mapToDouble(CartItem::vatTotal).sum();
  }

  public double discountTotal() {
    return items.stream().mapToDouble(CartItem::getDiscount).sum();
  }

  public void setCustomer(Customer customer) { this.customer = customer; }
  public Customer getCustomer() { return customer; }
}