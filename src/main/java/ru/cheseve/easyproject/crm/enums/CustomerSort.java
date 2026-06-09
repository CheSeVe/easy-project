package ru.cheseve.easyproject.crm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum CustomerSort {
    NAME(Sort.by(Sort.Direction.ASC, "name"));
    private final Sort sort;
}
