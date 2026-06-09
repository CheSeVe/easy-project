package ru.cheseve.easyproject.integration.subscriprion.event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.cheseve.easyproject.crm.dto.product.ProductCreatedEvent;
import ru.cheseve.easyproject.integration.subscriprion.service.ProductSubscriptionsService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCreatedEventListener {

    ProductSubscriptionsService subscriptionsService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductCreated(ProductCreatedEvent event) {
        subscriptionsService.createSubscriptionsForProduct(event.productId());
    }
}
