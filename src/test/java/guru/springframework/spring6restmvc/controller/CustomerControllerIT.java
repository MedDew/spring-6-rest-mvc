package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;


    @Test
    public void updateByIdNotFoundTest(){
        assertThrows(NotFoundException.class,() -> {
            customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @org.springframework.transaction.annotation.Transactional
    @Rollback
    @Test
    public void updateByIdTest(){

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(
                customerRepository.findAll().get(00)
        );
        String customerName = "NAME UPDATED";
        customerDTO.setCustomerName(customerName);


        ResponseEntity responseEntity = customerController.updateById(
                customerDTO.getId(), customerDTO
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Customer updatedCustomer = customerRepository.findById(customerDTO.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
        assertThat(updatedCustomer.getLastModifiededDate()).isAfter(customerDTO.getLastModifiededDate());
    }

    @Transactional
    @Rollback
    @Test
    public void handlePostTest(){

        CustomerDTO customerDTO = CustomerDTO.builder()
                        .customerName("Lenny")
                        .createdDate(LocalDateTime.now())
                        .lastModifiededDate(LocalDateTime.now())
                        .build();


        ResponseEntity responseEntity = customerController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().get("Location")).isNotNull();
        String[] uriParts = Objects.requireNonNull(responseEntity.getHeaders().getLocation())
                            .getPath().split("/");
        UUID createdUUID = UUID.fromString(uriParts[uriParts.length - 1]);
        assertThat(createdUUID).isNotNull();

        Customer savedCustomer = customerRepository.findById(createdUUID).get();
        assertThat(savedCustomer).isNotNull();
    }

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