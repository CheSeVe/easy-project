package ru.cheseve.easyproject.dto.order;

import org.springframework.format.annotation.DateTimeFormat;
import ru.cheseve.easyproject.enums.Status;

import java.time.Instant;

public record OrderFilterDTO(
        Status status,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant createdFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant createdTo
) {
}
