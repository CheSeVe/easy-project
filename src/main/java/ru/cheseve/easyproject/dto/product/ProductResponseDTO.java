package ru.cheseve.easyproject.dto.product;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price
) {
}
