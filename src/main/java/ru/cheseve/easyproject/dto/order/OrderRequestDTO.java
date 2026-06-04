package ru.cheseve.easyproject.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.cheseve.easyproject.enums.Status;

@Schema(description = "Параметры добавления заказа")
public record OrderRequestDTO(
        @Schema(description = "Статус заказа", example = "NEW")
        @NotNull
        Status status,

        @Schema(description = "Id клиента", example = "1")
        @NotNull
        Long customerId) {
}
