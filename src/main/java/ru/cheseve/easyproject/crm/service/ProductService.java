package ru.cheseve.easyproject.crm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.crm.dto.product.*;
import ru.cheseve.easyproject.crm.event.ProductChangedEvent;
import ru.cheseve.easyproject.dto.PageResponse;
import ru.cheseve.easyproject.crm.entity.Product;
import ru.cheseve.easyproject.crm.exception.NameAlreadyExistsException;
import ru.cheseve.easyproject.crm.exception.ProductNotFoundException;
import ru.cheseve.easyproject.crm.mapper.ProductMapper;
import ru.cheseve.easyproject.crm.repository.OrderItemRepository;
import ru.cheseve.easyproject.crm.repository.ProductRepository;
import ru.cheseve.easyproject.crm.specification.ProductSpecifications;
import ru.cheseve.easyproject.integration.subscriprion.enums.ProductEventType;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper mapper;
    OrderItemRepository orderItemRepository;
    ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        log.debug("getProduct id={}", id);
        Product product = getProductOrThrowException(id);

        return mapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(
            ProductFilterDTO filter,
            Pageable pageable
    ) {
        log.debug("getAllProducts page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<ProductResponse> productPage = productRepository
                .findAll(ProductSpecifications.withFilter(filter), pageable)
                .map(mapper::toResponse);

        return PageResponse.from(productPage);
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        log.debug("addProduct started");
        validateNameUniqueness(request.name());

        Product product = productRepository.save(mapper.toEntity(request));
        log.debug("addProduct - created id={}", product.getId());

        applicationEventPublisher.publishEvent(new ProductCreatedEvent(product.getId()));
        return mapper.toResponse(product);
    }

    @Transactional
    public ProductResponse putProduct(Long id, ProductRequest request) {
        log.debug("putProduct id={}", id);
        Product existingProduct = getProductOrThrowException(id);

        validateNameUniqueness(request.name(), existingProduct);

        List<ProductEventType> eventTypes = new ArrayList<>();

        if (!Objects.equals(request.name(), existingProduct.getName())) {
            eventTypes.add(ProductEventType.NAME_CHANGED);
        }

        if (!Objects.equals(request.description(), existingProduct.getDescription())) {
            eventTypes.add(ProductEventType.DESCRIPTION_CHANGED);
        }

        if (existingProduct.getPrice().compareTo(request.price()) != 0) {
            eventTypes.add(ProductEventType.PRICE_CHANGED);
        }

        mapper.updateProductFromRequest(request, existingProduct);

        eventTypes.forEach(eventType -> applicationEventPublisher
                .publishEvent(new ProductChangedEvent(id, eventType)));

        return mapper.toResponse(existingProduct);
    }

    @Transactional
    public ProductResponse patchProduct(Long id, ProductPatchRequest request) {
        log.debug("patchProduct id={}", id);
        Product existingProduct = getProductOrThrowException(id);

        List<ProductEventType> eventTypes = new ArrayList<>();

        if (request.name() != null) {
            validateNameUniqueness(request.name(), existingProduct);

            if (!Objects.equals(request.name(), existingProduct.getName())) {
                existingProduct.setName(request.name());
                eventTypes.add(ProductEventType.NAME_CHANGED);
            }
        }

        if (request.description() != null) {
            if (!Objects.equals(request.description(), existingProduct.getDescription())) {
                existingProduct.setDescription(request.description());
                eventTypes.add(ProductEventType.DESCRIPTION_CHANGED);
            }
        }

        if (request.price() != null) {
            if (request.price().compareTo(existingProduct.getPrice()) != 0) {
                existingProduct.setPrice(request.price());
                eventTypes.add(ProductEventType.PRICE_CHANGED);
            }
        }

        eventTypes.forEach(eventType -> applicationEventPublisher.publishEvent(
                new ProductChangedEvent(id, eventType)
        ));

        return mapper.toResponse(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.debug("deleteProduct id={}", id);

        if (orderItemRepository.existsByProduct_Id(id)) {
            throw new IllegalArgumentException(String.format(
                    "Product with id %d already used in orders", id));
        }

        Product product = getProductOrThrowException(id);
        productRepository.delete(product);
    }

    private Product getProductOrThrowException(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(
                        "Product with id %d does not exist", id
                )));
    }

    private void validateNameUniqueness(String name) {
        if (productRepository.existsByName(name)) {
            throw new NameAlreadyExistsException(String.format(
                    "Product with name %s already exists", name));
        }
    }

    private void validateNameUniqueness(String name, Product existingProduct) {
        if (name != null
                && !name.equals(existingProduct.getName())
                && productRepository.existsByName(name)) {
            throw new NameAlreadyExistsException(String.format(
                    "Product with name %s already exists", name));
        }
    }
}