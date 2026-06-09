package ru.cheseve.easyproject.crm.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Параметры фильтрации клиентов. Все поля опциональны.")
public record CustomerFilterDTO(
        @Schema(description = "Фильтр по имени", example = "Иван")
        String name,
        @Schema(description = "Фильтр по фамилии", example = "Иванов")
        String surname,
        @Schema(description = "Фильтр по email", example = "ivanovivan@example.com")
        String email,
        @Schema(description = "Фильтр по номеру телефона", example = "+79001234567")
        String phoneNumber
) {
    public boolean isEmpty() {
        return name == null
                && surname == null
                && email == null
                && phoneNumber == null;
    }
}
