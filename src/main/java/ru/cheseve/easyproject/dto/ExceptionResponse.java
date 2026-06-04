package ru.cheseve.easyproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Schema(description = "Ответ с информацией об ошибке")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    @Schema(description = "Сообщение об ошибке", example = "Validation failed")
    String message;
    @Schema(description = "Список ошибок валидации")
    List<FieldValidationError> errors;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    @Schema(description = "Ошибка валидации конкретного поля")
    public record FieldValidationError(
            @Schema(description = "Название поля", example = "email")
            String field,

            @Schema(description = "Описание ошибки", example = "must not be blank")
            String message
    ) {}
}
