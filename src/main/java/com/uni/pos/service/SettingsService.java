package com.uni.pos.service;

import com.uni.pos.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsService {
  public String get(String key, String def) {
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT value FROM settings WHERE key=?")) {
      ps.setString(1, key);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("value");
    } catch (Exception e) { e.printStackTrace(); }
    return def;
  }

  public void set(String key, String value) {
    try (Connection c = Database.getConnection()) {
      PreparedStatement ps = c.prepareStatement("""
        INSERT INTO settings(key,value) VALUES(?,?)
        ON CONFLICT(key) DO UPDATE SET value=excluded.value
      """ );
      ps.setString(1, key);
      ps.setString(2, value);
      ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); }
  }
}