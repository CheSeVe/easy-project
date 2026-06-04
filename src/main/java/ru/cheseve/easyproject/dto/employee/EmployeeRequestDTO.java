package ru.cheseve.easyproject.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import ru.cheseve.easyproject.enums.Role;

@Schema(description = "Параметры добавления сотрудника")
public record EmployeeRequestDTO(
        @Schema(description = "Имя сотрудника", example = "Иван")
        @NotBlank
        String name,

        @Schema(description = "Фамилия сотрудника", example = "Иванов")
        @NotBlank
        String surname,

        @Schema(description = "Email сотрудника", example = "ivanovivan@exampla.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Пароль сотрудника", example = "123456")
        @NotEmpty
        @Size(min = 6)
        String password,

        @Schema(description = "Роль сотрудника", example = "MANAGER")
        @NotNull
        Role role
) {
}
