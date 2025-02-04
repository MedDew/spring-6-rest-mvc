package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    private CustomerServiceImpl customerServiceImpl;

    @Captor
    ArgumentCaptor<UUID>  uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    public void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    public void updateCustomerPatchByIdTest() throws Exception {

        Customer customer = customerServiceImpl.listCustomers().get(0);

        Map<String, String> payload = new HashMap<>();
        payload.put("customerName", "MedGaz");

        mockMvc.perform(
                patch(CustomerController.CUSTOMER_PATH+"/"+customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        )
        .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(payload.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    @Test
    public void deleteByIdTest() throws Exception {

        Customer customer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(
                delete(CustomerController.CUSTOMER_PATH+"/"+customer.getId())
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerByid(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    public void updateByIdTest() throws Exception {
        Customer customerPayload =  customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(
                put(CustomerController.CUSTOMER_PATH+"/"+customerPayload.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerPayload))
        )
        .andExpect(
                status().isNoContent()
        );

        verify(customerService).updateCustomerById(customerPayload.getId(), customerPayload);
    }

    @Test
    public void handlePostTest() throws Exception {

        Customer customerPayload = customerServiceImpl.listCustomers().get(0);
        customerPayload.setId(null);

        given(customerService.saveNewCustomer(any(Customer.class)))
        .willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(
                post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerPayload))
        )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));

    }


    @Test
    void listCustomersTest() throws Exception {

        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(
                get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    public void getCustomerByIdTest() throws Exception {

        Customer testCustomer = customerServiceImpl.listCustomers().get(0);
        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(
                get(CustomerController.CUSTOMER_PATH+"/" + testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
        .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));

    }
}