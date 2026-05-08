package ru.cheseve.easyproject.dto.product;

import java.math.BigDecimal;

public record ProductFilterDTO(
        String name,
        BigDecimal priceFrom,
        BigDecimal priceTo
) {
}
