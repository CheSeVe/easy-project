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
import ru.cheseve.easyproject.crm.dto.product.*;
import ru.cheseve.easyproject.dto.ExceptionResponse;
import ru.cheseve.easyproject.dto.PageResponse;
import ru.cheseve.easyproject.crm.enums.ProductSort;
import ru.cheseve.easyproject.crm.service.ProductService;

@Tag(name = "Products", description = "Управление товарами")
@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/products")
public class ProductController {
    ProductService productService;

    @Operation(summary = "Получить товар по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.info("GET /api/products/{}", id);
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @Operation(summary = "Получить список всех товаров")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @ModelAttribute ProductFilterDTO filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "NAME_ASC") ProductSort sort
    ) {
        log.info("GET /api/products/, page={}, size={}, sort={}, filtered={}",
                page,
                size,
                sort,
                !filter.isEmpty());
        Pageable pageable = PageRequest.of(page, size, sort.getSort());
        return ResponseEntity.ok(productService.getAllProducts(filter, pageable));
    }

    @Operation(
            summary = "Добавить новый товар",
            description = "Добавляет новый товар и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Товар добавлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    ResponseEntity<ProductResponse> addProduct(@RequestBody @Valid ProductRequest request) {
        log.info("POST /api/products/");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(request));
    }

    @Operation(
            summary = "Полностью заменить существующий товар",
            description = "Заменяет товар и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар заменен"),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> putProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequest request) {
        log.info("PUT /api/products/{}", id);
        return ResponseEntity.ok(productService.putProduct(id, request));
    }

    @Operation(
            summary = "Частично заменить существующий товар",
            description = "Заменяет указанные в запросе поля у существующего товара и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные заменены"),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка во входных данных",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<ProductResponse> patchProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductPatchRequest request) {
        log.info("PATCH /api/products/{}", id);
        return ResponseEntity.ok(productService.patchProduct(id, request));
    }

    @Operation(summary = "Удалить товар по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Товар удален"),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{}", id);
        productService.deleteProduct(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
