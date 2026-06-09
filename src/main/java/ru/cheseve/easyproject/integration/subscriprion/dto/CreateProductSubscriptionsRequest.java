package ru.cheseve.easyproject.integration.subscriprion.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductSubscriptionsRequest(
        @NotEmpty
        List<@NotNull Long> customerIds
) {
}
