package ru.cheseve.easyproject.dto.customer;

import ru.cheseve.easyproject.dto.order.OrderResponseDTO;

import java.util.List;

public record CustomerWithOrdersResponseDTO(
        Long id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        List<OrderResponseDTO> orders
) {
}
