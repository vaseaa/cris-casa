package com.uni.pos.service;

import com.uni.pos.db.Database;
import com.uni.pos.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerService {
  private final ObservableList<Customer> customers = FXCollections.observableArrayList();

  public ObservableList<Customer> getCustomers() {
    reload();
    return customers;
  }

  public void reload() {
    customers.clear();
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT * FROM customer")) {
      ResultSet rs = ps.executeQuery();
      while (rs.next()) customers.add(map(rs));
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void save(Customer cst) {
    try (Connection c = Database.getConnection()) {
      if (cst.id() == 0) {
        PreparedStatement ps = c.prepareStatement("""
          INSERT INTO customer(name,phone,email,loyalty_points) VALUES(?,?,?,?)
        """);
        ps.setString(1, cst.name());
        ps.setString(2, cst.phone());
        ps.setString(3, cst.email());
        ps.setInt(4, cst.loyaltyPoints());
        ps.executeUpdate();
      } else {
        PreparedStatement ps = c.prepareStatement("""
          UPDATE customer SET name=?,phone=?,email=?,loyalty_points=? WHERE id=?
        """);
        ps.setString(1, cst.name());
        ps.setString(2, cst.phone());
        ps.setString(3, cst.email());
        ps.setInt(4, cst.loyaltyPoints());
        ps.setInt(5, cst.id());
        ps.executeUpdate();
      }
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void addPoints(int customerId, int points) {
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("UPDATE customer SET loyalty_points=loyalty_points+? WHERE id=?")) {
      ps.setInt(1, points);
      ps.setInt(2, customerId);
      ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); }
  }

  private Customer map(ResultSet rs) throws Exception {
    return new Customer(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("phone"),
        rs.getString("email"),
        rs.getInt("loyalty_points")
    );
  }
}