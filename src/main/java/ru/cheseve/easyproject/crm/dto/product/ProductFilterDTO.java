package ru.cheseve.easyproject.crm.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Параметры фильтрации товаров. Все поля опциональны.")
public record ProductFilterDTO(
        @Schema(description = "Фильтр по названию", example = "Ноутбук")
        String name,
        @Schema(description = "Фильтр по цене (от)", example = "1000.00")
        BigDecimal priceFrom,
        @Schema(description = "Фильтр по цене (до)", example = "1000.00")
        BigDecimal priceTo
) {
    public boolean isEmpty() {
        return name == null
                && priceFrom == null
                && priceTo == null;
    }
}
