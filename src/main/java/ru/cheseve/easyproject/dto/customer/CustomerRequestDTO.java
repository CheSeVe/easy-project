package ru.cheseve.easyproject.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.cheseve.easyproject.validation.ValidPhoneNumber;

@Schema(description = "Параметры добавления клиента")
public record CustomerRequestDTO(
        @Schema(description = "Имя клиента", example = "Иван")
        @NotBlank
        String name,

        @Schema(description = "Фамилия клиента", example = "Иванов")
        @NotBlank
        String surname,

        @Schema(description = "Email клиента", example = "ivanovivan@example.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Номер телефона клиента", example = "+79001234567")
        @NotBlank
        @ValidPhoneNumber
        String phoneNumber
) {
}
