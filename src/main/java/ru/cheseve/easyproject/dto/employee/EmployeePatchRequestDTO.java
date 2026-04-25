package ru.cheseve.easyproject.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ru.cheseve.easyproject.enums.Role;
import ru.cheseve.easyproject.validation.NotBlankOrNull;

public record EmployeePatchRequestDTO(
        @NotBlankOrNull String name,
        @NotBlankOrNull String surname,
        @NotBlankOrNull @Email String email,
        @Size(min = 6) String password,
        Role role
) {
}
