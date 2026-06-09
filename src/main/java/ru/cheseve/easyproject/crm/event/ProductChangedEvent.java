package ru.cheseve.easyproject.crm.event;

import ru.cheseve.easyproject.integration.subscriprion.enums.ProductEventType;

public record ProductChangedEvent(
        Long productId,
        ProductEventType eventType
) {
}
