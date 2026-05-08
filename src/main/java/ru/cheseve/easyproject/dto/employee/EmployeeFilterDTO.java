package ru.cheseve.easyproject.dto.employee;

import ru.cheseve.easyproject.enums.Role;

public record EmployeeFilterDTO(
    String name,
    String surname,
    String email,
    Role role
) {
}
