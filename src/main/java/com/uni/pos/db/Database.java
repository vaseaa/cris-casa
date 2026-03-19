package com.uni.pos.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
  private static final String DB_DIR = System.getProperty("user.home") + "/.pos-sim";
  private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/pos.db";

  static {
    try {
      Files.createDirectories(Path.of(DB_DIR));
    } catch (Exception ignored) {}
  }

  private Database() {}

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL);
  }
}