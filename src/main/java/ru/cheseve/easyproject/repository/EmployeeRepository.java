package ru.cheseve.easyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cheseve.easyproject.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
}
