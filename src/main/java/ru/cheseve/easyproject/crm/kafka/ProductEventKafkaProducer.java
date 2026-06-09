package ru.cheseve.easyproject.crm.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductEventKafkaProducer {

    private static final String TOPIC = "product-events";

    KafkaTemplate<String, ProductEventMessage> kafkaTemplate;

    public void send(ProductEventMessage message) {
        kafkaTemplate.send(TOPIC, message.productId().toString(), message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send product event message: {}", message, ex);
                        return;
                    }

                    log.info(
                            "Sent product event message: topic={}, partition={}, offset={}, key={}, message={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            message.productId(),
                            message
                    );
                });
    }
}
