package ru.cheseve.easyproject.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import ru.cheseve.easyproject.validation.NullOrNotBlank;
import ru.cheseve.easyproject.validation.ValidPhoneNumber;

@Schema(description = "Параметры частичного обновления клиента")
public record CustomerPatchRequestDTO(
        @Schema(description = "Имя клиента", example = "Иван", nullable = true)
        @NullOrNotBlank
        String name,

        @Schema(description = "Фамилия клиента", example = "Иванов", nullable = true)
        @NullOrNotBlank
        String surname,

        @Schema(description = "Email клиента", example = "ivanovivan@example.com", nullable = true)
        @NullOrNotBlank
        @Email
        String email,

        @Schema(description = "Номер телефона клиента", example = "+79001234567", nullable = true)
        @NullOrNotBlank
        @ValidPhoneNumber
        String phoneNumber
) {
}
