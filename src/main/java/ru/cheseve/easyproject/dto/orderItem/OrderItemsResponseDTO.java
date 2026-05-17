package ru.cheseve.easyproject.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Данные товаров в заказе")
public record OrderItemsResponseDTO(
        @Schema(description = "ID заказа", example = "1")
        Long orderId,

        @Schema(description = "Список позиций заказа")
        List<OrderItemResponseDTO> items
) {
}
