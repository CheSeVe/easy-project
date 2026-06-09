package ru.cheseve.easyproject.crm.dto.orderItem;

import ru.cheseve.easyproject.crm.entity.Product;

public record OrderItemReplacement(
        Product product,
        Integer quantity
) {
}
