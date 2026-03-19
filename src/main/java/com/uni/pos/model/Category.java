package com.uni.pos.model;

public record Category(int id, String name) {
  @Override public String toString() { return name; }
}