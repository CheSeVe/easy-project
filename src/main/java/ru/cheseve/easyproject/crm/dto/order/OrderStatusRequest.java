package ru.cheseve.easyproject.crm.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.cheseve.easyproject.crm.enums.Status;

@Schema(description = "Параметры изменения статуса заказа")
public record OrderStatusRequest(
        @Schema(description = "Статус заказа", example = "NEW")
        @NotNull
        Status status
) {
}
