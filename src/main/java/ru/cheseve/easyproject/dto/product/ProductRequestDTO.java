package ru.cheseve.easyproject.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Параметры добавления товара")
public record ProductRequestDTO(
        @Schema(description = "Название товара", example = "Ноутбук")
        @NotBlank
        String name,

        @Schema(description = "Описание товара", example = "Ноутбук 19\"")
        @NotBlank
        @Size(max = 255)
        String description,

        @Schema(description = "Цена товара", example = "1000.00")
        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price
) {
}
