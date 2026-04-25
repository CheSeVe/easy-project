package ru.cheseve.easyproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cheseve.easyproject.dto.employee.EmployeeRequestDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeResponseDTO;
import ru.cheseve.easyproject.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeResponseDTO toResponseDTO(Employee employee);
    Employee toEntity(EmployeeRequestDTO requestDTO);
    void updateEmployeeFromDto(EmployeeRequestDTO requestDTO, @MappingTarget Employee employee);
}
