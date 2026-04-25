package ru.cheseve.easyproject.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum EmployeeSort {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    NAME_ASC(Sort.by(Sort.Direction.ASC, "name")),
    SURNAME_ASC(Sort.by(Sort.Direction.ASC, "surname")),
    ROLE_ASC(Sort.by(Sort.Direction.ASC, "role"));

    private final Sort sort;
}
