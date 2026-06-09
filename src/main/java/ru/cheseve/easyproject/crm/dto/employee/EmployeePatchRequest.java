package ru.cheseve.easyproject.crm.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ru.cheseve.easyproject.crm.enums.Role;
import ru.cheseve.easyproject.validation.NullOrNotBlank;

@Schema(description = "Параметры частичного обновления сотрудника")
public record EmployeePatchRequest(
        @Schema(description = "Имя сотрудника", example = "Иван", nullable = true)
        @NullOrNotBlank
        String name,

        @Schema(description = "Фамилия сотрудника", example = "Иванов", nullable = true)
        @NullOrNotBlank
        String surname,

        @Schema(description = "Email сотрудника", example = "ivanovivan@exampla.com", nullable = true)
        @NullOrNotBlank
        @Email
        String email,

        @Schema(description = "Пароль сотрудника", example = "123456", nullable = true)
        @Size(min = 6)
        String password,

        @Schema(description = "Роль сотрудника", example = "MANAGER", nullable = true)
        Role role
) {
}
