package ru.cheseve.easyproject.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import ru.cheseve.easyproject.validation.NullOrNotBlank;

import java.math.BigDecimal;

@Schema(description = "Параметры частичного изменения товара")
public record ProductPatchRequestDTO(
        @Schema(description = "Название товара", example = "Ноутбук", nullable = true)
        @NullOrNotBlank
        String name,

        @Schema(description = "Описание товара", example = "Ноутбук 19\"", nullable = true)
        @NullOrNotBlank
        @Size(max = 255)
        String description,

        @Schema(description = "Цена товара", example = "1000.00", nullable = true)
        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price
) {
}
