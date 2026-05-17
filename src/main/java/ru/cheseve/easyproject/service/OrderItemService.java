package ru.cheseve.easyproject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.dto.orderItem.OrderItemReplacement;
import ru.cheseve.easyproject.dto.orderItem.OrderItemRequestDTO;
import ru.cheseve.easyproject.dto.orderItem.OrderItemsRequestDTO;
import ru.cheseve.easyproject.dto.orderItem.OrderItemsResponseDTO;
import ru.cheseve.easyproject.entity.Order;
import ru.cheseve.easyproject.entity.Product;
import ru.cheseve.easyproject.exception.OrderNotFoundException;
import ru.cheseve.easyproject.exception.ProductNotFoundException;
import ru.cheseve.easyproject.mapper.OrderItemMapper;
import ru.cheseve.easyproject.repository.OrderRepository;
import ru.cheseve.easyproject.repository.ProductRepository;

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
    public OrderItemsResponseDTO getItems(Long orderId) {
        log.debug("getItems orderId={}", orderId);
        Order order = getOrderWithItemsAndProductsOrThrowException(orderId);
        return orderItemMapper.toOrderItemsResponseDTO(order);
    }

    @Transactional
    public OrderItemsResponseDTO putItems(Long orderId, OrderItemsRequestDTO requestDTO) {
        log.debug("putItems orderId={}", orderId);
        Order order = getOrderWithItemsAndProductsOrThrowException(orderId);

        Map<Long, OrderItemReplacement> replacementByProductId = toReplacementMap(
                requestDTO.items(),
                getProductsByIdsOrThrowException(requestDTO.items()));

        order.replaceItems(replacementByProductId);

        return orderItemMapper.toOrderItemsResponseDTO(order);
    }

    @Transactional
    public OrderItemsResponseDTO patchItems(Long orderId, OrderItemsRequestDTO requestDTO) {
        log.debug("patchItems orderId={}", orderId);
        Order order = getOrderWithItemsAndProductsOrThrowException(orderId);

        Map<Long, Product> requestProductsById = getProductsByIdsOrThrowException(requestDTO.items());

        for (OrderItemRequestDTO itemRequest : requestDTO.items()) {
            Product product = requestProductsById.get(itemRequest.productId());
            order.upsertItem(product, itemRequest.quantity());
        }

        order.refreshItemsPriceFromProductPrice();

        return orderItemMapper.toOrderItemsResponseDTO(order);
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

    private Map<Long, Product> getProductsByIdsOrThrowException(List<OrderItemRequestDTO> items) {

        Set<Long> requestedProductIds = items.stream()
                .map(OrderItemRequestDTO::productId)
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
            List<OrderItemRequestDTO> items,
            Map<Long, Product> productsById) {

        return items.stream()
                .collect(Collectors.toMap(
                   OrderItemRequestDTO::productId,
                   item -> new OrderItemReplacement(
                           productsById.get(item.productId()),
                           item.quantity()
                   )
                ));
    }
}