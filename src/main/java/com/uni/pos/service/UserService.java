package com.uni.pos.service;

import com.uni.pos.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {
  public boolean authenticate(String username, String password) {
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT * FROM user WHERE username=? AND password=?")) {
      ps.setString(1, username);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (Exception e) { e.printStackTrace(); }
    return false;
  }
}