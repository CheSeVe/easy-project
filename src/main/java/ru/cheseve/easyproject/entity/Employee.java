package ru.cheseve.easyproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.cheseve.easyproject.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    Long id;
    String name;
    String surname;
    String email;
    String password;
    Role role;
}
