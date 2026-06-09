package ru.cheseve.easyproject.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cheseve.easyproject.crm.dto.employee.EmployeeRequest;
import ru.cheseve.easyproject.crm.dto.employee.EmployeeResponse;
import ru.cheseve.easyproject.crm.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeResponse toResponse(Employee employee);
    Employee toEntity(EmployeeRequest request);
    void updateEmployeeFromRequest(EmployeeRequest request, @MappingTarget Employee employee);
}
