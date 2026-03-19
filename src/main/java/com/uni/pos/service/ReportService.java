package com.uni.pos.service;

import com.uni.pos.db.Database;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportService {

  public String salesSummary() {
    StringBuilder sb = new StringBuilder();
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("""
           SELECT date(created_at) d, COUNT(*) cnt, SUM(total) total
           FROM receipt WHERE refunded=0 GROUP BY d ORDER BY d DESC
         """)) {
      ResultSet rs = ps.executeQuery();
      sb.append("Data | Bonuri | Total\n");
      while (rs.next()) {
        sb.append(rs.getString("d")).append(" | ")
          .append(rs.getInt("cnt")).append(" | ")
          .append(String.format("%.2f", rs.getDouble("total")))
          .append("\n");
      }
    } catch (Exception e) { e.printStackTrace(); }
    return sb.toString();
  }

  public String topProducts() {
    StringBuilder sb = new StringBuilder();
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement("""
           SELECT name, SUM(qty) q
           FROM receipt_item GROUP BY name ORDER BY q DESC LIMIT 10
         """)) {
      ResultSet rs = ps.executeQuery();
      sb.append("Produs | Cantitate\n");
      while (rs.next()) {
        sb.append(rs.getString("name")).append(" | ")
          .append(rs.getInt("q")).append("\n");
      }
    } catch (Exception e) { e.printStackTrace(); }
    return sb.toString();
  }

  public void exportToFile(String content, String path) {
    try (FileWriter fw = new FileWriter(path)) {
      fw.write(content);
    } catch (Exception e) { e.printStackTrace(); }
  }
}