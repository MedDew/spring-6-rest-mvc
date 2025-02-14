package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void listCustomersTest(){
        List<CustomerDTO> customerDTOList = customerController.listCustomers();

        assertThat(customerDTOList.size()).isEqualTo(3);
    }

    @Transactional
    @Rollback
    @Test
    public void listCustomersEmptyTest(){
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOList = customerController.listCustomers();

        assertThat(customerDTOList.size()).isEqualTo(0);
    }

    @Test
    public void getCustomerByIdTest(){
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertThat(customerDTO).isNotNull();
    }

    @Test
    public void getCustomerByIdNotFoundTest(){
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });

    }
}