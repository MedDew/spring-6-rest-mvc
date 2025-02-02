package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {


    @Autowired
    private MockMvc mockMvc;

//    @MockBean => deprecated
    @MockitoBean
    BeerService beerService;

    @Test
    void getBeerById() throws Exception {

        ResultMatcher resultMatcher = m -> m.getResponse().getContentType()
                                      .equals(MediaType.APPLICATION_JSON);


        mockMvc.perform(get("/api/v1/beer/"+UUID.randomUUID()))
                //.andExpect(resultMatcher.match())
                .andExpect(status().isOk());
    }
}