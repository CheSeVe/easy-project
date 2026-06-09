package ru.cheseve.easyproject.crm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.crm.dto.orderItem.*;
import ru.cheseve.easyproject.crm.entity.Order;
import ru.cheseve.easyproject.crm.entity.Product;
import ru.cheseve.easyproject.crm.exception.OrderNotFoundException;
import ru.cheseve.easyproject.crm.exception.ProductNotFoundException;
import ru.cheseve.easyproject.crm.mapper.OrderItemMapper;
import ru.cheseve.easyproject.crm.repository.OrderRepository;
import ru.cheseve.easyproject.crm.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    OrderItemMapper orderItemMapper;

    @Transactional(readOnly = true)
    public OrderItemsResponse getItems(Long orderId) {
        log.debug("getItems orderId={}", orderId);
        Order order = getOrderWithItemsAndProductsOrThrowException(orderId);
        return orderItemMapper.toOrderItemsResponse(order);
    }

    @Transactional
    public OrderItemsResponse putItems(Long orderId, OrderItemsRequest request) {
        log.debug("putItems orderId={}", orderId);
        Order order = getOrderWithItemsAndProductsOrThrowException(orderId);

        Map<Long, OrderItemReplacement> replacementByProductId = toReplacementMap(
                request.items(),
                getProductsByIdsOrThrowException(request.items()));

        order.replaceItems(replacementByProductId);

        return orderItemMapper.toOrderItemsResponse(order);
    }

    @Transactional
    public OrderItemsResponse patchItems(Long orderId, OrderItemsRequest request) {
        log.debug("patchItems orderId={}", orderId);
        Order order = getOrderWithItemsAndProductsOrThrowException(orderId);

        Map<Long, Product> requestProductsById = getProductsByIdsOrThrowException(request.items());

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = requestProductsById.get(itemRequest.productId());
            order.upsertItem(product, itemRequest.quantity());
        }

        order.refreshItemsPriceFromProductPrice();

        return orderItemMapper.toOrderItemsResponse(order);
    }

    @Transactional
    public void deleteItem(Long orderId, Long productId) {
        log.debug("deleteItem, orderId={}, productId={}", orderId, productId);
        Order order = getOrderWithItemsOrThrowException(orderId);
        order.removeItem(productId);
    }

    private Order getOrderWithItemsAndProductsOrThrowException(Long id) {
        return orderRepository.findWithItemsAndProductsById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order with id %d does not exist", id)));
    }

    private Order getOrderWithItemsOrThrowException(Long id) {
        return orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order with id %d does not exist", id)));
    }

    private Map<Long, Product> getProductsByIdsOrThrowException(List<OrderItemRequest> items) {

        Set<Long> requestedProductIds = items.stream()
                .map(OrderItemRequest::productId)
                .collect(Collectors.toSet());

        if (items.size() != requestedProductIds.size()) {
            throw new IllegalArgumentException("duplicate productId in request");
        }

        Map<Long, Product> productsById = productRepository.findAllById(requestedProductIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<Long> foundProductsIds = productsById.keySet();

        Set<Long> missingProducts = requestedProductIds.stream()
                .filter(productId -> !foundProductsIds.contains(productId))
                .collect(Collectors.toSet());

        if (!missingProducts.isEmpty()) {
            throw new ProductNotFoundException("products not found: " + missingProducts);
        }
        return productsById;
    }

    private Map<Long, OrderItemReplacement> toReplacementMap(
            List<OrderItemRequest> items,
            Map<Long, Product> productsById) {

        return items.stream()
                .collect(Collectors.toMap(
                   OrderItemRequest::productId,
                   item -> new OrderItemReplacement(
                           productsById.get(item.productId()),
                           item.quantity()
                   )
                ));
    }
}