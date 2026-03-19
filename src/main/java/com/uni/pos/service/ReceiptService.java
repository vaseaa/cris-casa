package com.uni.pos.service;

import com.uni.pos.db.Database;
import com.uni.pos.model.CartItem;
import com.uni.pos.model.Customer;
import com.uni.pos.model.Receipt;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptService {
  private final ProductService productService;

  public ReceiptService(ProductService productService) {
    this.productService = productService;
  }

  public Receipt finalizeReceipt(List<CartItem> items, Customer customer, String method,
                                 double cashReceived) {
    double total = items.stream().mapToDouble(CartItem::lineTotal).sum();
    double vat = items.stream().mapToDouble(CartItem::vatTotal).sum();
    double disc = items.stream().mapToDouble(CartItem::getDiscount).sum();
    double change = Math.max(0, cashReceived - total);
    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    try (Connection c = Database.getConnection()) {
      c.setAutoCommit(false);

      PreparedStatement ps = c.prepareStatement("""
        INSERT INTO receipt(created_at,customer_id,total,vat_total,discount_total,
        payment_method,cash_received,change_given,refunded) VALUES(?,?,?,?,?,?,?,?,0)
      "", Statement.RETURN_GENERATED_KEYS);

      ps.setString(1, now);
      if (customer == null) ps.setNull(2, Types.INTEGER);
      else ps.setInt(2, customer.id());
      ps.setDouble(3, total);
      ps.setDouble(4, vat);
      ps.setDouble(5, disc);
      ps.setString(6, method);
      ps.setDouble(7, cashReceived);
      ps.setDouble(8, change);
      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      int receiptId = rs.next() ? rs.getInt(1) : 0;

      PreparedStatement itemPs = c.prepareStatement("""
        INSERT INTO receipt_item(receipt_id,product_id,name,qty,price,vat,discount)
        VALUES(?,?,?,?,?,?,?)
      """ );

      for (CartItem i : items) {
        itemPs.setInt(1, receiptId);
        itemPs.setInt(2, i.getProduct().id());
        itemPs.setString(3, i.getProduct().name());
        itemPs.setInt(4, i.getQty());
        itemPs.setDouble(5, i.getProduct().price());
        itemPs.setDouble(6, i.getProduct().vat());
        itemPs.setDouble(7, i.getDiscount());
        itemPs.addBatch();

        productService.updateStock(i.getProduct().id(), -i.getQty());
      }

      itemPs.executeBatch();
      c.commit();

      return new Receipt(receiptId, now, customer == null ? null : customer.id(),
          total, vat, disc, method, cashReceived, change, false);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void refund(int receiptId) {
    try (Connection c = Database.getConnection()) {
      c.setAutoCommit(false);

      PreparedStatement mark = c.prepareStatement("UPDATE receipt SET refunded=1 WHERE id=?");
      mark.setInt(1, receiptId);
      mark.executeUpdate();

      PreparedStatement ps = c.prepareStatement("SELECT product_id, qty FROM receipt_item WHERE receipt_id=?");
      ps.setInt(1, receiptId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        productService.updateStock(rs.getInt("product_id"), rs.getInt("qty"));
      }

      c.commit();
    } catch (Exception e) { e.printStackTrace(); }
  }
}