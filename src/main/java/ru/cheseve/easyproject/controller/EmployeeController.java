package ru.cheseve.easyproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cheseve.easyproject.dto.ExceptionResponse;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeFilterDTO;
import ru.cheseve.easyproject.dto.employee.EmployeePatchRequestDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeResponseDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeRequestDTO;
import ru.cheseve.easyproject.enums.EmployeeSort;
import ru.cheseve.easyproject.service.EmployeeService;

@Tag(name = "Employees", description = "Управление сотрудниками")
@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/employees")
public class EmployeeController {

    EmployeeService employeeService;

    @Operation(summary = "Получить сотрудника по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Long id) {
        log.info("GET /api/employees/{}", id);
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @Operation(summary = "Получить список всех сотрудников")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<PageResponseDTO<EmployeeResponseDTO>> getAllEmployees(
            @ModelAttribute EmployeeFilterDTO filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "ID_ASC") EmployeeSort sort
    ) {
        log.info("GET /api/employees/, page={}, size={}, sort={}, filtered={}",
                page,
                size,
                sort,
                !filter.isEmpty());
        Pageable pageable = PageRequest.of(page, size, sort.getSort());
        return ResponseEntity.ok(employeeService.getAllEmployees(filter, pageable));
    }

    @Operation(
            summary = "Добавить нового сотрудника",
            description = "Добавляет сотрудника и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Сотрудник создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Сотрудник с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody @Valid EmployeeRequestDTO requestDTO) {
        log.info("POST /api/employees/");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(requestDTO));
    }

    @Operation(
            summary = "Полностью заменить существуюшего сотрудника",
            description = "Заменяет существующего сотрудника и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сотрудник заменен"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Сотрудник с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> putEmployee(@PathVariable Long id,
                                                           @RequestBody @Valid EmployeeRequestDTO requestDTO) {
        log.info("PUT /api/employees/{}", id);
        return ResponseEntity.ok(employeeService.putEmployee(id, requestDTO));
    }

    @Operation(
            summary = "Частично заменить существующего сотрудника",
            description = "Заменяет указанные в запросе поля у существующего сотрудника и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные у сотрудника заменены"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Сотрудник с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> patchEmployee(@PathVariable Long id,
                                                             @RequestBody @Valid EmployeePatchRequestDTO requestDTO) {
        log.info("PATCH /api/employees/{}", id);
        return ResponseEntity.ok(employeeService.patchEmployee(id, requestDTO));
    }

    @Operation(summary = "Удалить сотрудника по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Сотрудник удален"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("DELETE /api/employees/{}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
