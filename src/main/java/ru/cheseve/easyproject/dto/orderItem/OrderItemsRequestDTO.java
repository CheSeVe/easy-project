package ru.cheseve.easyproject.dto.orderItem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderItemsRequestDTO(
        @NotEmpty
        List<@Valid OrderItemRequestDTO> items
) {
}
