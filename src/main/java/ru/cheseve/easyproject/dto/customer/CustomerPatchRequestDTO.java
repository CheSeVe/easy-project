package ru.cheseve.easyproject.dto.customer;

import jakarta.validation.constraints.Email;
import ru.cheseve.easyproject.validation.NullOrNotBlank;
import ru.cheseve.easyproject.validation.ValidPhoneNumber;

public record CustomerPatchRequestDTO(
        @NullOrNotBlank
        String name,

        @NullOrNotBlank
        String surname,

        @NullOrNotBlank
        @Email
        String email,

        @NullOrNotBlank
        @ValidPhoneNumber
        String phoneNumber
) {
}
