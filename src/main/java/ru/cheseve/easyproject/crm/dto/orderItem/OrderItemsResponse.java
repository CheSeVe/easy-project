package ru.cheseve.easyproject.crm.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Данные товаров в заказе")
public record OrderItemsResponse(
        @Schema(description = "ID заказа", example = "1")
        Long orderId,

        @Schema(description = "Список позиций заказа")
        List<OrderItemResponse> items
) {
}
