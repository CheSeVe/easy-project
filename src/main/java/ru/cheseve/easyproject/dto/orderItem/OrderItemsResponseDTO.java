package ru.cheseve.easyproject.dto.orderItem;

import java.util.List;

public record OrderItemsResponseDTO(
        Long orderId,
        List<OrderItemResponseDTO> items
) {
}
