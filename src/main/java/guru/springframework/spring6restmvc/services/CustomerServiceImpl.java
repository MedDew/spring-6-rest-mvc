package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {

        CustomerDTO customer1 = CustomerDTO.builder()
                            .id(UUID.randomUUID())
                            .customerName("Mehdi")
                            .version(1)
                            .createdDate(LocalDateTime.now())
                            .lastModifiededDate(LocalDateTime.now())
                            .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Estelle")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiededDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
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
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }

        if(customer.getVersion() != 0) {
            existingCustomer.setVersion(customer.getVersion());
        }

        if(customer.getLastModifiededDate() != null) {
            existingCustomer.setLastModifiededDate(customer.getLastModifiededDate());
        }

        return Optional.of(existingCustomer);
    }

    @Override
    public Boolean deleteCustomerByid(UUID id) {
        return customerMap.remove(id) != null;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);
        existing.setCustomerName(customer.getCustomerName());
        existing.setVersion(customer.getVersion());
        existing.setLastModifiededDate(LocalDateTime.now());

        return Optional.of(existing);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        log.info("Number of customers in DB: " + customerMap.size());
        return List.of(customerMap.values().toArray(new CustomerDTO[0]));
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        log.info("Selected customer by UUID: " + uuid);
        return Optional.of(customerMap.get(uuid));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        CustomerDTO customerSaved = CustomerDTO.builder()
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
