package ru.cheseve.easyproject.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ru.cheseve.easyproject.enums.Role;
import ru.cheseve.easyproject.validation.NullOrNotBlank;

public record EmployeePatchRequestDTO(
        @NullOrNotBlank
        String name,

        @NullOrNotBlank
        String surname,

        @NullOrNotBlank
        @Email
        String email,

        @Size(min = 6)
        String password,

        Role role
) {
}
