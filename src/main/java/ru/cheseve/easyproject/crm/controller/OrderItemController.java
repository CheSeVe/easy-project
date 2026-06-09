package ru.cheseve.easyproject.crm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cheseve.easyproject.crm.dto.orderItem.OrderItemsResponse;
import ru.cheseve.easyproject.dto.ExceptionResponse;
import ru.cheseve.easyproject.crm.dto.orderItem.OrderItemsRequest;
import ru.cheseve.easyproject.crm.service.OrderItemService;

@Tag(name = "Order items", description = "Управление товарами внутри заказа")
@Slf4j
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/orders/{orderId}/items")
public class OrderItemController {

    OrderItemService orderItemService;

    @Operation(summary = "Получить список всех товаров в заказе по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    public ResponseEntity<OrderItemsResponse> getAllItems(@PathVariable Long orderId) {
        log.info("GET /api/orders/{}/items", orderId);
        return ResponseEntity.ok(orderItemService.getItems(orderId));
    }

    @Operation(
            summary = "Установить список товаров в заказе",
            description = "Устанавливает список товаров из запроса с актуальной ценой товаров"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список товаров сохранен"),
            @ApiResponse(responseCode = "404", description = "Заказ либо товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping
    public ResponseEntity<OrderItemsResponse> putItems(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderItemsRequest request
    ) {
        log.info("PUT /api/orders/{}/items", orderId);
        return ResponseEntity.ok(orderItemService.putItems(orderId, request));
    }

    @Operation(
            summary = "Добавить новый товар в заказ и/или заменить количество у существующих",
            description = "Заменяет количество товаров в заказе на полученное в запросе и актуализирует цену всех предметов в заказе"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список изменен"),
            @ApiResponse(responseCode = "404", description = "Заказ либо товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<OrderItemsResponse> patchItems(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderItemsRequest requestDTO
    ) {
        log.info("PATCH /api/orders/{}/items", orderId);
        return ResponseEntity.ok(orderItemService.patchItems(orderId, requestDTO));
    }

    @Operation(summary = "Удалить товар из заказа по ID товара")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Товар удален"),
            @ApiResponse(responseCode = "404", description = "Заказ или товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long orderId,
            @PathVariable Long productId
    ) {
        log.info("DELETE /api/orders/{}/items/{}", orderId, productId);
        orderItemService.deleteItem(orderId, productId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
