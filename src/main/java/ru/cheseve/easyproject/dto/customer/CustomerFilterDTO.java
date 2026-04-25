package ru.cheseve.easyproject.dto.customer;

public record CustomerFilterDTO(
        String name,
        String surname,
        String email,
        String phoneNumber
) {
}
