package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {

        Customer customer1 = Customer.builder()
                            .id(UUID.randomUUID())
                            .customerName("Mehdi")
                            .version(1)
                            .createdDate(LocalDateTime.now())
                            .lastModifiededDate(LocalDateTime.now())
                            .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Estelle")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiededDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Rayan")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiededDate(LocalDateTime.now())
                .build();



        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }

        if(customer.getVersion() != 0) {
            existingCustomer.setVersion(customer.getVersion());
        }

        if(customer.getLastModifiededDate() != null) {
            existingCustomer.setLastModifiededDate(customer.getLastModifiededDate());
        }
    }

    @Override
    public void deleteCustomerByid(UUID id) {
        customerMap.remove(id);
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);
        existing.setCustomerName(customer.getCustomerName());
        existing.setVersion(customer.getVersion());
        existing.setLastModifiededDate(LocalDateTime.now());

    }

    @Override
    public List<Customer> listCustomers() {
        log.info("Number of customers in DB: " + customerMap.size());
        return List.of(customerMap.values().toArray(new Customer[0]));
    }

    @Override
    public Customer getCustomerById(UUID uuid) {
        log.info("Selected customer by UUID: " + uuid);
        return customerMap.get(uuid);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {

        Customer customerSaved = Customer.builder()
                                .id(UUID.randomUUID())
                                .createdDate(LocalDateTime.now())
                                .lastModifiededDate(LocalDateTime.now())
                                .customerName(customer.getCustomerName())
                                .version(customer.getVersion())
                                .build();

        customerMap.put(customerSaved.getId(), customerSaved);

        return customerSaved;
    }
}
