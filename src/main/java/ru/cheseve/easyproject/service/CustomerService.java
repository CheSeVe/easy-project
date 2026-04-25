package ru.cheseve.easyproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.customer.*;
import ru.cheseve.easyproject.entity.Customer;
import ru.cheseve.easyproject.exception.EmailAlreadyExistsException;
import ru.cheseve.easyproject.exception.PhoneNumberAlreadyExistsException;
import ru.cheseve.easyproject.mapper.CustomerMapper;
import ru.cheseve.easyproject.repository.CustomerRepository;
import ru.cheseve.easyproject.repository.OrderRepository;
import ru.cheseve.easyproject.specification.CustomerSpecifications;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper mapper;
    OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public CustomerWithOrdersResponseDTO getCustomerWithOrders(Long id) {
        Customer customer = getCustomerWithOrdersOrThrowException(id);

        return mapper.toResponseWithOrders(customer);
    }

    private Customer getCustomerWithOrdersOrThrowException(Long id) {
        return customerRepository.findWithOrdersById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Customer with id %d does not exist", id)));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<CustomerResponseDTO> getAllCustomers(
            CustomerFilterDTO filter,
            Pageable pageable) {
        Page<CustomerResponseDTO> customerPage = customerRepository
                .findAll(CustomerSpecifications.withFilter(filter), pageable)
                .map(mapper::toResponse);

        return PageResponseDTO.from(customerPage);
    }

    @Transactional
    public CustomerResponseDTO addCustomer(CustomerRequestDTO requestDTO) {
        validateEmailUniqueness(requestDTO.email());
        validatePhoneNumberUniqueness(requestDTO.phoneNumber());

        Customer customer = customerRepository.save(mapper.toEntity(requestDTO));

        return mapper.toResponse(customer);
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

    @Transactional
    public CustomerResponseDTO putCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer existingCustomer = getCustomerOrThrowException(id);
        validateEmailUniqueness(requestDTO.email(), existingCustomer);
        validatePhoneNumberUniqueness(requestDTO.phoneNumber(), existingCustomer);

        mapper.updateCustomerFromRequest(requestDTO, existingCustomer);

        return mapper.toResponse(existingCustomer);
    }

    private Customer getCustomerOrThrowException(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
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


    @Transactional
    public CustomerResponseDTO patchCustomer(Long id, CustomerPatchRequestDTO requestDTO) {
        Customer existingCustomer = getCustomerOrThrowException(id);

        if (requestDTO.name() != null && !requestDTO.name().isBlank()) {
            existingCustomer.setName(requestDTO.name());
        }

        if (requestDTO.surname() != null && !requestDTO.surname().isBlank()) {
            existingCustomer.setSurname(requestDTO.surname());
        }

        if (requestDTO.email() != null && !requestDTO.email().isBlank()) {
            validateEmailUniqueness(requestDTO.email(), existingCustomer);
            existingCustomer.setEmail(requestDTO.email());
        }

        if (requestDTO.phoneNumber() != null && !requestDTO.phoneNumber().isBlank()) {
            validatePhoneNumberUniqueness(requestDTO.phoneNumber(), existingCustomer);
            existingCustomer.setPhoneNumber(requestDTO.phoneNumber());
        }
        return mapper.toResponse(existingCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerOrThrowException(id);
        orderRepository.detachCustomerFromOrders(id);

        customerRepository.delete(customer);
    }


}
