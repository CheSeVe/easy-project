package ru.cheseve.easyproject.dto;

import jakarta.validation.constraints.*;
import ru.cheseve.easyproject.enums.Role;

public record EmployeeRequestDTO(
        @NotBlank String name,
        @NotBlank String surname,
        @NotBlank @Email String email,
        @NotEmpty @Size(min = 6) String password,
        @NotNull Role role
) {
}
