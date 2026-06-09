package ru.cheseve.easyproject.crm.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.cheseve.easyproject.crm.event.ProductChangedEvent;

import java.time.Instant;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class ProductChangedEventListener {

    ProductEventKafkaProducer eventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishToKafka(ProductChangedEvent event) {
        eventPublisher.send(new ProductEventMessage(
                UUID.randomUUID(),
                event.productId(),
                event.eventType(),
                Instant.now()));
    }
}
