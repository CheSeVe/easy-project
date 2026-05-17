package ru.cheseve.easyproject.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные клиента")
public record CustomerResponseDTO(
        @Schema(description = "ID клиента", example = "1")
        Long id,

        @Schema(description = "Имя клиента", example = "Иван")
        String name,

        @Schema(description = "Фамилия клиента", example = "Иванов")
        String surname,

        @Schema(description = "Email клиента", example = "ivanovivan@example.com")
        String email,

        @Schema(description = "Номер телефона клиента", example = "+79001234567")
        String phoneNumber
) {
}
