package com.uni.pos.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public final class Schema {
  private Schema() {}

  public static void init() {
    try (Connection c = Database.getConnection(); Statement s = c.createStatement()) {
      s.execute("""
        CREATE TABLE IF NOT EXISTS category(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT UNIQUE NOT NULL
        )
      """);

      s.execute("""
        CREATE TABLE IF NOT EXISTS product(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL,
          barcode TEXT UNIQUE,
          category_id INTEGER,
          price REAL NOT NULL,
          vat REAL NOT NULL,
          stock INTEGER NOT NULL,
          active INTEGER NOT NULL DEFAULT 1,
          FOREIGN KEY(category_id) REFERENCES category(id)
        )
      """);

      s.execute("""
        CREATE TABLE IF NOT EXISTS customer(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL,
          phone TEXT,
          email TEXT,
          loyalty_points INTEGER NOT NULL DEFAULT 0
        )
      """);

      s.execute("""
        CREATE TABLE IF NOT EXISTS receipt(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          created_at TEXT NOT NULL,
          customer_id INTEGER,
          total REAL NOT NULL,
          vat_total REAL NOT NULL,
          discount_total REAL NOT NULL,
          payment_method TEXT NOT NULL,
          cash_received REAL NOT NULL,
          change_given REAL NOT NULL,
          refunded INTEGER NOT NULL DEFAULT 0,
          FOREIGN KEY(customer_id) REFERENCES customer(id)
        )
      """);

      s.execute("""
        CREATE TABLE IF NOT EXISTS receipt_item(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          receipt_id INTEGER NOT NULL,
          product_id INTEGER NOT NULL,
          name TEXT NOT NULL,
          qty INTEGER NOT NULL,
          price REAL NOT NULL,
          vat REAL NOT NULL,
          discount REAL NOT NULL,
          FOREIGN KEY(receipt_id) REFERENCES receipt(id)
        )
      """);

      s.execute("""
        CREATE TABLE IF NOT EXISTS settings(
          key TEXT PRIMARY KEY,
          value TEXT NOT NULL
        )
      """);

      s.execute("""
        CREATE TABLE IF NOT EXISTS user(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          username TEXT UNIQUE NOT NULL,
          password TEXT NOT NULL,
          role TEXT NOT NULL
        )
      """
      );

      seedIfEmpty(c);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void seedIfEmpty(Connection c) throws Exception {
    try (Statement s = c.createStatement()) {
      ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM user");
      if (rs.next() && rs.getInt(1) == 0) {
        s.execute("INSERT INTO user(username,password,role) VALUES('admin','admin123','ADMIN')");
      }

      rs = s.executeQuery("SELECT COUNT(*) FROM category");
      if (rs.next() && rs.getInt(1) == 0) {
        s.execute("INSERT INTO category(name) VALUES('Bauturi'),('Alimente'),('Igiena'),('Electro')");
      }

      rs = s.executeQuery("SELECT COUNT(*) FROM product");
      if (rs.next() && rs.getInt(1) == 0) {
        s.execute("""
          INSERT INTO product(name,barcode,category_id,price,vat,stock,active) VALUES
          ('Apa plata 2L','123456789001',1,4.50,0.09,120,1),
          ('Cafea 250g','123456789002',2,17.90,0.09,60,1),
          ('Sapun lichid','123456789003',3,12.40,0.19,40,1),
          ('Casti audio','123456789004',4,149.00,0.19,15,1)
        """);
      }

      rs = s.executeQuery("SELECT COUNT(*) FROM settings");
      if (rs.next() && rs.getInt(1) == 0) {
        s.execute("INSERT INTO settings(key,value) VALUES('store_name','POS Universitar'),('currency','RON')");
      }
    }
  }
}