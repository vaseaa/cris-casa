package com.uni.pos.model;

public class CartItem {
  private final Product product;
  private int qty;
  private double discount;

  public CartItem(Product product, int qty) {
    this.product = product;
    this.qty = qty;
  }
  public Product getProduct() { return product; }
  public int getQty() { return qty; }
  public void setQty(int qty) { this.qty = qty; }
  public double getDiscount() { return discount; }
  public void setDiscount(double discount) { this.discount = discount; }

  public double lineTotal() {
    return Math.max(0, (product.price() * qty) - discount);
  }

  public double vatTotal() {
    return lineTotal() * product.vat();
  }
}