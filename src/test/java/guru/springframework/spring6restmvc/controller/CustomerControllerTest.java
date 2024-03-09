package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void listCustomers() throws Exception {
        //ARRANGE
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());
        MockHttpServletRequestBuilder mockRequest = get("/api/v1/customer").accept(MediaType.APPLICATION_JSON);

        //ACT
        ResultActions resultActions = mockMvc.perform(mockRequest)

        //ASSERT
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

        //log
        MvcResult result = resultActions.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        log.info(jsonResponse);

    }

    @Test
    @Disabled
    void getCustomerById() throws Exception {

        // Arrange
        CustomerDTO customerDTO = customerServiceImpl.listCustomers().get(0);
        log.info(customerDTO.toString());

        MockHttpServletRequestBuilder mockRequest = get(CustomerController.CUSTOMER_PATH, customerDTO.getId()).accept(MediaType.APPLICATION_JSON);

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.of(customerDTO));

        // Act
        ResultActions resultActions = mockMvc.perform(mockRequest)
        // Assert
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.name", is(customerDTO.getName())));

        // Convert the JSON response into a string and log
        log.info(resultActions.andReturn().getResponse().getContentAsString());
    }
}