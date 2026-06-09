package ru.cheseve.easyproject.crm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.crm.dto.customer.*;
import ru.cheseve.easyproject.dto.PageResponse;
import ru.cheseve.easyproject.crm.entity.Customer;
import ru.cheseve.easyproject.crm.exception.CustomerNotFoundException;
import ru.cheseve.easyproject.crm.exception.EmailAlreadyExistsException;
import ru.cheseve.easyproject.crm.exception.PhoneNumberAlreadyExistsException;
import ru.cheseve.easyproject.crm.mapper.CustomerMapper;
import ru.cheseve.easyproject.crm.repository.CustomerRepository;
import ru.cheseve.easyproject.crm.repository.OrderRepository;
import ru.cheseve.easyproject.crm.specification.CustomerSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper mapper;
    OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public CustomerWithOrdersResponse getCustomerWithOrders(Long id) {
        log.debug("getCustomerWithOrders id={}", id);
        Customer customer = getCustomerWithOrdersOrThrowException(id);
        return mapper.toResponseWithOrders(customer);
    }

    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> getAllCustomers(
            CustomerFilterDTO filter,
            Pageable pageable) {
        log.debug("getAllCustomers page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<CustomerResponse> customerPage = customerRepository
                .findAll(CustomerSpecifications.withFilter(filter), pageable)
                .map(mapper::toResponse);

        return PageResponse.from(customerPage);
    }

    @Transactional
    public CustomerResponse addCustomer(CustomerRequest request) {
        log.debug("addCustomer started");
        validateEmailUniqueness(request.email());
        validatePhoneNumberUniqueness(request.phoneNumber());

        Customer customer = customerRepository.save(mapper.toEntity(request));

        log.debug("addCustomer - created id={}", customer.getId());
        return mapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse putCustomer(Long id, CustomerRequest request) {
        log.debug("putCustomer id={}", id);

        Customer existingCustomer = getCustomerOrThrowException(id);
        validateEmailUniqueness(request.email(), existingCustomer);
        validatePhoneNumberUniqueness(request.phoneNumber(), existingCustomer);

        mapper.updateCustomerFromRequest(request, existingCustomer);

        return mapper.toResponse(existingCustomer);
    }

    @Transactional
    public CustomerResponse patchCustomer(Long id, CustomerPatchRequest request) {
        log.debug("patchCustomer id={}", id);

        Customer existingCustomer = getCustomerOrThrowException(id);

        if (request.name() != null) {
            existingCustomer.setName(request.name());
        }

        if (request.surname() != null) {
            existingCustomer.setSurname(request.surname());
        }

        if (request.email() != null) {
            validateEmailUniqueness(request.email(), existingCustomer);
            existingCustomer.setEmail(request.email());
        }

        if (request.phoneNumber() != null) {
            validatePhoneNumberUniqueness(request.phoneNumber(), existingCustomer);
            existingCustomer.setPhoneNumber(request.phoneNumber());
        }
        return mapper.toResponse(existingCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.debug("deleteCustomer id={}", id);

        Customer customer = getCustomerOrThrowException(id);
        orderRepository.detachCustomerFromOrders(id);

        customerRepository.delete(customer);
    }

    private Customer getCustomerWithOrdersOrThrowException(Long id) {
        return customerRepository.findWithOrdersById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with id %d does not exist", id)));
    }

    private void validateEmailUniqueness(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format(
                    "Customer with email %s already exists", email));
        }
    }

    private void validatePhoneNumberUniqueness(String phoneNumber) {
        if (customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNumberAlreadyExistsException(String.format(
                    "Customer with phone number %s already exists", phoneNumber));
        }
    }

    private Customer getCustomerOrThrowException(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with id %d does not exist", id)));
    }

    private void validateEmailUniqueness(String email, Customer existingCustomer) {
        if (email != null
                && !email.equals(existingCustomer.getEmail())
                && customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format(
                    "Customer with email %s already exists", email));
        }
    }

    private void validatePhoneNumberUniqueness(String phoneNumber, Customer existingCustomer) {
        if (phoneNumber != null
                && !phoneNumber.equals(existingCustomer.getPhoneNumber())
                && customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNumberAlreadyExistsException(String.format(
                    "Customer with phone number %s already exists", phoneNumber));
        }
    }
}
