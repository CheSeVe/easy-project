package ru.cheseve.easyproject.dto.order;

import jakarta.validation.constraints.NotNull;
import ru.cheseve.easyproject.enums.Status;

public record OrderStatusRequestDTO(
        @NotNull Status status
) {
}
