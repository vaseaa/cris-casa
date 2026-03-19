package com.uni.pos.model;

public record Product(
    int id,
    String name,
    String barcode,
    int categoryId,
    double price,
    double vat,
    int stock,
    boolean active
) {}