package ru.cheseve.easyproject.integration.subscriprion.dto;

import ru.cheseve.easyproject.integration.subscriprion.enums.ProductEventType;

public record CreateProductSubscriptionsResponse(
        int requestedCount,
        int insertedCount,
        int skippedCount,
        ProductEventType productEventType
) {
}
