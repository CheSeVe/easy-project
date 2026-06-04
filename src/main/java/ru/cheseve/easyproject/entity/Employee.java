package ru.cheseve.easyproject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.cheseve.easyproject.enums.Role;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Long id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "surname", nullable = false)
    String surname;
    @Column(name = "email", nullable = false, unique = true)
    String email;
    @Column(name = "password", nullable = false)
    String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    Role role;
}
