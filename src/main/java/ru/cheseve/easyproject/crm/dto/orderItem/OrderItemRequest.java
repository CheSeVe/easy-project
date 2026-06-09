package ru.cheseve.easyproject.crm.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Позиция в заказе")
public record OrderItemRequest(
        @Schema(description = "ID товара", example = "1")
        @NotNull
        Long productId,

        @Schema(description = "Количество товара", example = "2")
        @NotNull
        @Min(1)
        Integer quantity
) {
}
