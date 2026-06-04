package ru.cheseve.easyproject.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Список товаров в заказе")
public record OrderItemsRequestDTO(
        @Schema(description = "Товары в заказе")
        @NotEmpty
        List<@Valid OrderItemRequestDTO> items
) {
}
