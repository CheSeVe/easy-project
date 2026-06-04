package ru.cheseve.easyproject.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.cheseve.easyproject.enums.Status;

import java.time.Instant;

@Schema(description = "Данные заказа")
public record OrderResponseDTO(
        @Schema(description = "ID заказа", example = "1")
        Long id,

        @Schema(description = "Статус заказа", example = "NEW")
        Status status,

        @Schema(description = "Дата и время создания заказа", example = "2026-01-01T10:30:00.000000000Z")
        Instant createdAt
) {
}
