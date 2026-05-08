package ru.cheseve.easyproject.dto.orderItem;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long productId,
        String name,
        Integer quantity,
        BigDecimal unitPrice
) {
}
