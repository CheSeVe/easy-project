package ru.cheseve.easyproject.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.cheseve.easyproject.dto.customer.CustomerRequestDTO;
import ru.cheseve.easyproject.dto.customer.CustomerResponseDTO;
import ru.cheseve.easyproject.dto.order.OrderRequestDTO;
import ru.cheseve.easyproject.enums.Status;
import ru.cheseve.easyproject.service.CustomerService;
import ru.cheseve.easyproject.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Profile("dev")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DevDataInitializer implements CommandLineRunner {

    CustomerService customerService;
    OrderService orderService;

    List<CustomerResponseDTO> customers = new ArrayList<>();


    @Override
    public void run(String... args) {
        for (int i = 0; i < 50; i++) {
            CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                    "Customer" + i, "Customerov" + i, "email" + i + "@mail.ru", "+123456789" + i);

            customers.add(customerService.addCustomer(requestDTO));
        }

        Status[] statuses = Status.values();

        for (int i = 0; i < 100; i++) {
            Status randomStatus = statuses[
                    ThreadLocalRandom.current().nextInt(statuses.length)
                    ];

            CustomerResponseDTO randomCustomer = customers.get(
                    ThreadLocalRandom.current().nextInt(customers.size())
            );
            OrderRequestDTO requestDTO = new OrderRequestDTO(
                    randomStatus,
                    randomCustomer.id()
            );

            orderService.addOrder(requestDTO);
        }
    }
}
