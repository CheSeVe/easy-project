package ru.cheseve.easyproject.crm.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import ru.cheseve.easyproject.crm.enums.Status;

import java.time.Instant;

@Schema(description = "Параметры фильтрации заказов. Все поля опциональны.")
public record OrderFilterDTO(
        @Schema(description = "Фильтр по статусу", example = "NEW")
        Status status,

        @Schema(description = "Фильтр по дате создания (после)", example = "2026-01-01T00:00:00Z")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant createdFrom,

        @Schema(description = "Фильтр по дате создания (до)", example = "2026-01-01T00:00:00Z")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant createdTo,

        @Schema(description = "Фильтр по наличию товара в заказе", example = "1")
        Long productId
) {
    public boolean isEmpty() {
        return status == null
                && createdFrom == null
                && createdTo == null
                && productId == null;
    }
}
