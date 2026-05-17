package ru.cheseve.easyproject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.customer.*;
import ru.cheseve.easyproject.entity.Customer;
import ru.cheseve.easyproject.exception.CustomerNotFoundException;
import ru.cheseve.easyproject.exception.EmailAlreadyExistsException;
import ru.cheseve.easyproject.exception.PhoneNumberAlreadyExistsException;
import ru.cheseve.easyproject.mapper.CustomerMapper;
import ru.cheseve.easyproject.repository.CustomerRepository;
import ru.cheseve.easyproject.repository.OrderRepository;
import ru.cheseve.easyproject.specification.CustomerSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper mapper;
    OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public CustomerWithOrdersResponseDTO getCustomerWithOrders(Long id) {
        log.debug("getCustomerWithOrders id={}", id);
        Customer customer = getCustomerWithOrdersOrThrowException(id);
        return mapper.toResponseWithOrders(customer);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<CustomerResponseDTO> getAllCustomers(
            CustomerFilterDTO filter,
            Pageable pageable) {
        log.debug("getAllCustomers page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<CustomerResponseDTO> customerPage = customerRepository
                .findAll(CustomerSpecifications.withFilter(filter), pageable)
                .map(mapper::toResponse);

        return PageResponseDTO.from(customerPage);
    }

    @Transactional
    public CustomerResponseDTO addCustomer(CustomerRequestDTO requestDTO) {
        log.debug("addCustomer started");
        validateEmailUniqueness(requestDTO.email());
        validatePhoneNumberUniqueness(requestDTO.phoneNumber());

        Customer customer = customerRepository.save(mapper.toEntity(requestDTO));

        log.debug("addCustomer - created id={}", customer.getId());
        return mapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponseDTO putCustomer(Long id, CustomerRequestDTO requestDTO) {
        log.debug("putCustomer id={}", id);

        Customer existingCustomer = getCustomerOrThrowException(id);
        validateEmailUniqueness(requestDTO.email(), existingCustomer);
        validatePhoneNumberUniqueness(requestDTO.phoneNumber(), existingCustomer);

        mapper.updateCustomerFromRequest(requestDTO, existingCustomer);

        return mapper.toResponse(existingCustomer);
    }

    @Transactional
    public CustomerResponseDTO patchCustomer(Long id, CustomerPatchRequestDTO requestDTO) {
        log.debug("patchCustomer id={}", id);

        Customer existingCustomer = getCustomerOrThrowException(id);

        if (requestDTO.name() != null) {
            existingCustomer.setName(requestDTO.name());
        }

        if (requestDTO.surname() != null) {
            existingCustomer.setSurname(requestDTO.surname());
        }

        if (requestDTO.email() != null) {
            validateEmailUniqueness(requestDTO.email(), existingCustomer);
            existingCustomer.setEmail(requestDTO.email());
        }

        if (requestDTO.phoneNumber() != null) {
            validatePhoneNumberUniqueness(requestDTO.phoneNumber(), existingCustomer);
            existingCustomer.setPhoneNumber(requestDTO.phoneNumber());
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
