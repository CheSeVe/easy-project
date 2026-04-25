package ru.cheseve.easyproject.dto.customer;

public record CustomerResponseDTO(
        Long id,
        String name,
        String surname,
        String email,
        String phoneNumber
) {
}
