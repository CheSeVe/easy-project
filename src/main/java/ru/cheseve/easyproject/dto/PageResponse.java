package ru.cheseve.easyproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Постраничный ответ")
public record PageResponse<T>(
        @Schema(description = "Список элементов на странице")
        List<T> content,

        @Schema(description = "Номер текущей страницы", example = "0")
        int page,

        @Schema(description = "Размер страницы", example = "10")
        int size,

        @Schema(description = "Общее количество элементов", example = "52")
        long totalElements,

        @Schema(description = "Общее количество страниц", example = "6")
        long totalPages
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
