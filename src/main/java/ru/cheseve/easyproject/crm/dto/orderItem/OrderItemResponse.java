package ru.cheseve.easyproject.crm.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Данные позиции заказа")
public record OrderItemResponse(
        @Schema(description = "ID товара", example = "1")
        Long productId,

        @Schema(description = "Название товара", example = "Ноутбук")
        String name,

        @Schema(description = "Количество товаров в заказе", example = "2")
        Integer quantity,

        @Schema(description = "Цена товара в заказе", example = "1000.00")
        BigDecimal unitPrice
) {
}
