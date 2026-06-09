package ru.cheseve.easyproject.integration.subscriprion.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cheseve.easyproject.crm.repository.CustomerRepository;
import ru.cheseve.easyproject.integration.subscriprion.SubsClient;
import ru.cheseve.easyproject.integration.subscriprion.dto.CreateProductSubscriptionsResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductSubscriptionsService {

    CustomerRepository customerRepository;
    SubsClient subsClient;

    public void createSubscriptionsForProduct(Long productId) {
        List<Long> customerIds = customerRepository.findAllIds();
        if (customerIds.isEmpty()) {
            log.info("No customers found, skipping subscription creation for productId={}", productId);
            return;
        }

        CreateProductSubscriptionsResponse response = subsClient.createProductSubscriptions(productId, customerIds);

        log.info(
                "Created product subscriptions. productId={}, requestedCount={}, insertedCount={}, skippedCount={}, eventType={}",
                productId,
                response.requestedCount(),
                response.insertedCount(),
                response.skippedCount(),
                response.productEventType()
        );
    }
}

