package ru.cheseve.easyproject.dto.product;

import jakarta.validation.constraints.*;
import ru.cheseve.easyproject.validation.NullOrNotBlank;

import java.math.BigDecimal;

public record ProductPatchRequestDTO(
        @NullOrNotBlank
        String name,

        @NullOrNotBlank
        @Size(max = 255)
        String description,

        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price
) {
}
