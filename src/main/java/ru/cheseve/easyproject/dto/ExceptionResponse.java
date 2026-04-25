package ru.cheseve.easyproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    String message;
    List<FieldValidationError> errors;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public record FieldValidationError(
            String field,
            String message
    ) {}
}
