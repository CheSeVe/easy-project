package ru.cheseve.easyproject.mapper;

import ru.cheseve.easyproject.dto.EmployeeRequestDTO;
import ru.cheseve.easyproject.dto.EmployeeResponseDTO;
import ru.cheseve.easyproject.entity.Employee;

public class EmployeeMapper {

    public static EmployeeResponseDTO mapEntityToResponse(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .email(employee.getEmail())
                .role(employee.getRole())
                .build();
    }

    public static Employee mapRequestToEntity(EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee();
        employee.setName(requestDTO.name());
        employee.setSurname(requestDTO.surname());
        employee.setEmail(requestDTO.email());
        employee.setRole(requestDTO.role());
        return employee;
    }
}
