package ru.cheseve.easyproject.crm.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Данные товара")
public record ProductResponse(
        @Schema(description = "ID товара", example = "1")
        Long id,

        @Schema(description = "Название товара", example = "Ноутбук")
        String name,

        @Schema(description = "Описание товара", example = "Ноутбук 19\"")
        String description,

        @Schema(description = "Цена товара", example = "1000.00")
        BigDecimal price
) {
}
