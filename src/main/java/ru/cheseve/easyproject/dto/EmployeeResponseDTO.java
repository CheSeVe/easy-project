package ru.cheseve.easyproject.dto;

import lombok.Builder;
import ru.cheseve.easyproject.enums.Role;

@Builder
public record EmployeeResponseDTO(
        Long id,
        String name,
        String surname,
        String email,
        Role role
) {

}