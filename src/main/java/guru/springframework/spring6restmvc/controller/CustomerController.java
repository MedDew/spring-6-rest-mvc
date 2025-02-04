package guru.springframework.spring6restmvc.controller;


import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH+"/{customerId}";

    @Autowired()
    private final CustomerService customerService;

    @PatchMapping(path = {CUSTOMER_PATH+"/{id}"})
    public ResponseEntity updateCustomerPatchById(@PathVariable(name = "id") UUID customerId, @RequestBody Customer customer) {

        customerService.patchCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = {CUSTOMER_PATH_ID})
    public ResponseEntity<String> deleteById(@PathVariable(value = "customerId") UUID id) {
        customerService.deleteCustomerByid(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateById(@PathVariable UUID customerId, @RequestBody Customer customer) {

        customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        Customer customerSaved = customerService.saveNewCustomer(customer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/customer/" + customerSaved.getId());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }


    @GetMapping(path = {CUSTOMER_PATH})
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(path = CUSTOMER_PATH_ID)
    public Customer getCustomerById(@PathVariable UUID customerId) {
        return customerService.getCustomerById(customerId);
    }
}
