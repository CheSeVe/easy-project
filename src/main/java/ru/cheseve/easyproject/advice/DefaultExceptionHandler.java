package ru.cheseve.easyproject.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cheseve.easyproject.dto.ExceptionResponse;
import ru.cheseve.easyproject.exception.*;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.core.JacksonException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleConflict(AlreadyExistsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ExceptionResponse.FieldValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        new ExceptionResponse.FieldValidationError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()))
                .toList();
        ExceptionResponse exceptionResponse = new ExceptionResponse("Validation failed");
        exceptionResponse.setErrors(errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return findCause(ex, InvalidFormatException.class)
                .filter(cause -> cause.getTargetType() != null)
                .filter(cause -> cause.getTargetType().isEnum())
                .map(this::handleInvalidEnumValue)
                .orElseGet(() -> ResponseEntity
                        .badRequest()
                        .body(new ExceptionResponse("Invalid request body")));
    }

    private <T extends Throwable> Optional<T> findCause(Throwable throwable, Class<T> targetType) {
        while (throwable != null) {
            if (targetType.isInstance(throwable)) {
                return Optional.of(targetType.cast(throwable));
            }

            throwable = throwable.getCause();
        }
        return Optional.empty();
    }

    private ResponseEntity<ExceptionResponse> handleInvalidEnumValue(
            InvalidFormatException ex
    ) {
        String fieldName = extractFieldName(ex);
        Object rejectedValue = ex.getValue();
        Class<?> enumType = ex.getTargetType();

        String allowedValues = Arrays.stream(enumType.getEnumConstants())
                .map(value -> ((Enum<?>) value).name())
                .collect(Collectors.joining(", "));

        String errorMessage = String.format(
                "Invalid value '%s'. Allowed values: %s", rejectedValue, allowedValues);

        ExceptionResponse response = new ExceptionResponse("Invalid request body");
        response.setErrors(List.of(
                new ExceptionResponse.FieldValidationError(fieldName, errorMessage)
        ));

        return ResponseEntity.badRequest().body(response);
    }

    private String extractFieldName(InvalidFormatException ex) {
        String fieldName = null;

        for (JacksonException.Reference reference : ex.getPath()) {
            if (reference.getPropertyName() != null) {
                fieldName = reference.getPropertyName();
            }
        }
        return fieldName != null ? fieldName : "unknown";
    }
}
