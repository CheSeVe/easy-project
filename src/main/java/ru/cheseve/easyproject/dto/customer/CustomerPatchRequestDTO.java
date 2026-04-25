package ru.cheseve.easyproject.dto.customer;

import jakarta.validation.constraints.Email;
import ru.cheseve.easyproject.validation.NotBlankOrNull;
import ru.cheseve.easyproject.validation.ValidPhoneNumber;

public record CustomerPatchRequestDTO(
        @NotBlankOrNull String name,
        @NotBlankOrNull String surname,
        @NotBlankOrNull @Email String email,
        @NotBlankOrNull @ValidPhoneNumber String phoneNumber
) {
}
