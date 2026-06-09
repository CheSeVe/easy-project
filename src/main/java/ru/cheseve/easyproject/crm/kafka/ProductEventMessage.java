package ru.cheseve.easyproject.crm.kafka;

import ru.cheseve.easyproject.integration.subscriprion.enums.ProductEventType;

import java.time.Instant;
import java.util.UUID;

public record ProductEventMessage(
        UUID eventId,
        Long productId,
        ProductEventType eventType,
        Instant occurredAt
) {
}
