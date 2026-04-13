package ru.cheseve.easyproject.repository;

import ru.cheseve.easyproject.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    void deleteById(Long id);

    boolean existsByEmail(String email);
}
