package ru.cheseve.easyproject.dto.orderItem;

import ru.cheseve.easyproject.entity.Product;

public record OrderItemReplacement(Product product, Integer quantity) {}
