package ru.cheseve.easyproject.dto.order;

import ru.cheseve.easyproject.enums.Status;

import java.time.Instant;

public record OrderWithCustomerIdResponseDTO(
        Long id,
        Status status,
        Instant createdAt,
        Long customerId
) {
}
