package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.cheseve.easyproject.controller.EmployeeController;
import ru.cheseve.easyproject.dto.employee.EmployeeResponseDTO;
import ru.cheseve.easyproject.enums.Role;
import ru.cheseve.easyproject.service.EmployeeService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    EmployeeService service;

    @InjectMocks
    EmployeeController controller;

    @Test
    @DisplayName("GET /api/{id} возвращает HTTP-ответ со статусом 200 и сотрудником, если он есть")
    void getEmployee_ReturnsValidResponseEntity() {
        //given
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(2L, "Еблан", "Ебланович", "eblan@mail.ru", Role.ADMIN);
        doReturn(responseDTO).when(service).getEmployee(2L);
        //when
        ResponseEntity<EmployeeResponseDTO> responseEntity = controller.getEmployee(2L);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDTO, responseEntity.getBody());
    }
}
