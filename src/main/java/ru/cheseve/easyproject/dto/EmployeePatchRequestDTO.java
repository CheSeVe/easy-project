package ru.cheseve.easyproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ru.cheseve.easyproject.enums.Role;

public record EmployeePatchRequestDTO(
        String name,
        String surname,
        @Email String email,
        @Size(min = 6) String password,
        Role role
) {
}
