package ru.cheseve.easyproject.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.cheseve.easyproject.enums.Role;

@Schema(description = "Параметры фильтрации сотрудников. Все поля опциональны.")
public record EmployeeFilterDTO(
        @Schema(description = "Фильтр по имени", example = "Иван")
        String name,
        @Schema(description = "Фильтр по фамилии", example = "Иванов")
        String surname,
        @Schema(description = "Фильтр по email", example = "ivanovivan@example.com")
        String email,
        @Schema(description = "Фильтр по роли", example = "MANAGER")
        Role role
) {

    public boolean isEmpty() {
        return name == null
                && surname == null
                && email == null
                && role == null;
    }
}
