package com.uni.pos.model;

public record Customer(int id, String name, String phone, String email, int loyaltyPoints) {
  @Override public String toString() { return name + " (" + loyaltyPoints + "p)"; }
}