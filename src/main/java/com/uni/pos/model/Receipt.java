package com.uni.pos.model;

public record Receipt(
    int id, String createdAt, Integer customerId, double total, double vatTotal,
    double discountTotal, String paymentMethod, double cashReceived, double changeGiven,
    boolean refunded
) {}