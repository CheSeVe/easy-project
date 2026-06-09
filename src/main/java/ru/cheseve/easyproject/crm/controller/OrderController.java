package ru.cheseve.easyproject.crm.controller;

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
import ru.cheseve.easyproject.crm.dto.order.*;
import ru.cheseve.easyproject.dto.ExceptionResponse;
import ru.cheseve.easyproject.dto.PageResponse;
import ru.cheseve.easyproject.crm.enums.OrderSort;
import ru.cheseve.easyproject.crm.service.OrderService;

@Tag(name = "Orders", description = "Управление заказами")
@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/orders")
public class OrderController {

    OrderService orderService;

    @Operation(
            summary = "Получить заказ по ID",
            description = "Возвращает заказ с данными клиента"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderWithCustomerResponse> getOrder(@PathVariable Long orderId) {
        log.info("GET /api/orders/{}", orderId);
        return ResponseEntity.ok(orderService.getOrderWithCustomer(orderId));
    }

    @Operation(summary = "Получить список всех заказов")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<PageResponse<OrderWithCustomerIdResponse>> getAllOrders(
            @ModelAttribute OrderFilterDTO filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "CREATED_ASC") OrderSort sort
    ) {
        log.info("GET /api/orders/, page={}, size={}, sort={}, filtered={}",
                page,
                size,
                sort,
                !filter.isEmpty());
        Pageable pageable = PageRequest.of(page, size, sort.getSort());

        return ResponseEntity.ok(orderService.getAllOrders(filter, pageable));
    }

    @Operation(
            summary = "Добавить новый заказ",
            description = "Добавляет заказ и возвращает его данные с ID клиента"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заказ создан"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PostMapping
    public ResponseEntity<OrderWithCustomerIdResponse> addOrder(@RequestBody @Valid OrderRequest request) {
        log.info("POST /api/orders/");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.addOrder(request));
    }


    @Operation(
            summary = "Поменять статус заказа",
            description = "Изменяет статус заказа и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус изменен"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusRequest request) {
        log.info("PATCH /api/orders/{}", orderId);
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }

    @Operation(summary = "Удалить заказ по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Заказ удален"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        log.info("DELETE /api/orders/{}", orderId);
        orderService.deleteOrder(orderId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
