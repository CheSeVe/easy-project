package ru.cheseve.easyproject.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank
        String name,

        @NotBlank
        @Size(max = 255)
        String description,

        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price
) {
}
