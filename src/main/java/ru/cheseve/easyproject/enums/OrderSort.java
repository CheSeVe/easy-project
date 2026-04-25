package ru.cheseve.easyproject.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum OrderSort {
    CREATED_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_DESC(Sort.by(Sort.Direction.DESC, "createdAt"));

    private final Sort sort;
}
