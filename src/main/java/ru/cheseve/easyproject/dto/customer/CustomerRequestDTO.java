package ru.cheseve.easyproject.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.cheseve.easyproject.validation.ValidPhoneNumber;

public record CustomerRequestDTO(
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @ValidPhoneNumber
        String phoneNumber
) {
}
