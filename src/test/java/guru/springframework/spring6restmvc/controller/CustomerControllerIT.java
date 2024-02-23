package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.CustomerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @Test
    void listCustomerHP() {
        List<CustomerDTO> customerDTOS = customerService.listCustomers();
        customerDTOS.forEach((customerDTO) -> {
            log.info(customerDTO.toString());
        });
        assertThat(customerDTOS.size()).isEqualTo(3);
    }

    @Transactional
    @Test
    @Order(1)
    void listCustomerUP() {
        List<Customer> allCustomers = customerRepository.findAll();
        allCustomers.forEach((customer) -> {
            log.info(customer.toString());
        });
        customerRepository.deleteAll();
        assertThat(customerRepository.count()).isEqualTo(0);
    }
}