package com.uni.pos.service;

import com.uni.pos.db.Database;
import com.uni.pos.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductService {
  private final ObservableList<Product> products = FXCollections.observableArrayList();

  public ObservableList<Product> getProducts() {
    reload();
    return products;
  }

  public void reload() {
    products.clear();
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT * FROM product")) {
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        products.add(map(rs));
      }
    } catch (Exception e) { e.printStackTrace(); }
  }

  public Product findByBarcode(String barcode) {
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT * FROM product WHERE barcode=? AND active=1")) {
      ps.setString(1, barcode);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return map(rs);
    } catch (Exception e) { e.printStackTrace(); }
    return null;
  }

  public void save(Product p) {
    try (Connection c = Database.getConnection()) {
      if (p.id() == 0) {
        PreparedStatement ps = c.prepareStatement("""
          INSERT INTO product(name,barcode,category_id,price,vat,stock,active)
          VALUES(?,?,?,?,?,?,?)
        """);
        ps.setString(1, p.name());
        ps.setString(2, p.barcode());
        ps.setInt(3, p.categoryId());
        ps.setDouble(4, p.price());
        ps.setDouble(5, p.vat());
        ps.setInt(6, p.stock());
        ps.setInt(7, p.active() ? 1 : 0);
        ps.executeUpdate();
      } else {
        PreparedStatement ps = c.prepareStatement("""
          UPDATE product SET name=?,barcode=?,category_id=?,price=?,vat=?,stock=?,active=? WHERE id=?
        """);
        ps.setString(1, p.name());
        ps.setString(2, p.barcode());
        ps.setInt(3, p.categoryId());
        ps.setDouble(4, p.price());
        ps.setDouble(5, p.vat());
        ps.setInt(6, p.stock());
        ps.setInt(7, p.active() ? 1 : 0);
        ps.setInt(8, p.id());
        ps.executeUpdate();
      }
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void updateStock(int productId, int delta) {
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("UPDATE product SET stock = stock + ? WHERE id=?")) {
      ps.setInt(1, delta);
      ps.setInt(2, productId);
      ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); }
  }

  private Product map(ResultSet rs) throws Exception {
    return new Product(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("barcode"),
        rs.getInt("category_id"),
        rs.getDouble("price"),
        rs.getDouble("vat"),
        rs.getInt("stock"),
        rs.getInt("active") == 1
    );
  }
}