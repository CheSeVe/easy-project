package ru.cheseve.easyproject.mapper;

import ru.cheseve.easyproject.dto.EmployeeRequestDTO;
import ru.cheseve.easyproject.dto.EmployeeResponseDTO;
import ru.cheseve.easyproject.entity.Employee;

public class EmployeeMapper {

    public static EmployeeResponseDTO mapEntityToResponse(Employee employee) {
        if (employee == null) {
             throw new IllegalArgumentException("Employee can't be null");
        }
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .email(employee.getEmail())
                .role(employee.getRole())
                .build();
    }

    public static Employee mapRequestToEntity(EmployeeRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new IllegalArgumentException("DTO can't be null");
        }
        Employee employee = new Employee();
        employee.setName(requestDTO.name());
        employee.setSurname(requestDTO.surname());
        employee.setEmail(requestDTO.email());
        employee.setPassword(requestDTO.password());
        employee.setRole(requestDTO.role());
        return employee;
    }
}
