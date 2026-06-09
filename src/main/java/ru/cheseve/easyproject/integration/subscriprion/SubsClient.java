package ru.cheseve.easyproject.integration.subscriprion;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import ru.cheseve.easyproject.integration.subscriprion.dto.CreateProductSubscriptionsResponse;
import ru.cheseve.easyproject.integration.subscriprion.exception.SubsClientException;
import ru.cheseve.easyproject.integration.subscriprion.dto.CreateProductSubscriptionsRequest;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SubsClient {

    RestClient subsRestClient;

    public SubsClient(@Qualifier("subsRestClient") RestClient subsRestClient) {
        this.subsRestClient = subsRestClient;
    }

    public CreateProductSubscriptionsResponse createProductSubscriptions(Long productId, List<Long> customerIds) {
        try {
            return subsRestClient.post()
                    .uri("/api/products/{productId}/subscriptions", productId)
                    .body(new CreateProductSubscriptionsRequest(customerIds))
                    .retrieve()
                    .body(CreateProductSubscriptionsResponse.class);
        } catch (RestClientResponseException ex) {
            throw new SubsClientException(
                    "Subs returned error while creating subscriptions for productId=%d, status=%s, body=%s"
                    .formatted(productId, ex.getStatusCode(), ex.getResponseBodyAsString()),
                    ex);
        } catch (RestClientException ex) {
            throw new SubsClientException(
                    "Failed to call subs while creating subscriptions for product with id=" + productId,
                    ex);
        }
    }
}
