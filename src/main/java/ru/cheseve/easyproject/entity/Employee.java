package ru.cheseve.easyproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cheseve.easyproject.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    Long id;
    String name;
    String surname;
    String email;
    String password;
    Role role;
}
