package ru.cheseve.easyproject.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.cheseve.easyproject.enums.Role;

@Schema(description = "Данные сотрудника")
public record EmployeeResponseDTO(
        @Schema(description = "ID сотрудника", example = "1")
        Long id,

        @Schema(description = "Имя сотрудника", example = "Иван")
        String name,

        @Schema(description = "Фамилия сотрудника", example = "Иванов")
        String surname,

        @Schema(description = "Email сотрудника", example = "ivanovivan@example.com")
        String email,

        @Schema(description = "Роль сотрудника", example = "MANAGER")
        Role role
) {

}