package ru.cheseve.easyproject.repository;

import org.springframework.stereotype.Repository;
import ru.cheseve.easyproject.entity.Employee;
import ru.cheseve.easyproject.exception.RepositoryException;

import java.util.*;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final Map<Long, Employee> employeeMap = new HashMap<>();

    long id;

    public List<Employee> findAll() {
        return new ArrayList<>(employeeMap.values());
    }

    public Employee save(Employee employee) {
        if (employee.getId() == null)
            employee.setId(id++);

        employeeMap.put(employee.getId(), employee);
        return employee;
    }

    public boolean existsByEmail(String email) {
        if (email == null)
            throw new RepositoryException("Email cannot be null");
        return employeeMap.values().stream()
                .anyMatch(e -> email.equals(e.getEmail()));
    }

    public void deleteById(Long id) {
        if (!employeeMap.containsKey(id))
            throw new RepositoryException(String.format(
                    "Employee with id %d does not exist",
                    id
            ));
        employeeMap.remove(id);
    }

    public Optional<Employee> findById(Long id) {
        return Optional.empty();
    }

}
