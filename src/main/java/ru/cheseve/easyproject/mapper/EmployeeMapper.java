package ru.cheseve.easyproject.mapper;

import org.mapstruct.Mapper;
import ru.cheseve.easyproject.dto.EmployeeRequestDTO;
import ru.cheseve.easyproject.dto.EmployeeResponseDTO;
import ru.cheseve.easyproject.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeResponseDTO toResponseDTO(Employee employee);
    Employee toEntity(EmployeeRequestDTO requestDTO);
}
