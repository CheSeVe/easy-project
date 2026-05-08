package ru.cheseve.easyproject.dto.order;

import ru.cheseve.easyproject.dto.customer.CustomerResponseDTO;
import ru.cheseve.easyproject.enums.Status;

import java.time.Instant;

public record OrderWithCustomerResponseDTO(
        Long id,
        Status status,
        Instant createdAt,
        CustomerResponseDTO customer
) {
}
