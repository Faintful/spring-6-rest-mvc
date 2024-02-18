package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
    void getCustomerById() throws Exception {

        // Arrange
        CustomerDTO customerDTO = customerServiceImpl.listCustomers().get(0);

        MockHttpServletRequestBuilder mockRequest = get("/api/v1/customerDTO/" + customerDTO.getId().toString()).accept(MediaType.APPLICATION_JSON);

        given(customerService.getCustomerById(customerDTO.getId())).willReturn(customerDTO);

        // Act
        ResultActions resultActions = mockMvc.perform(mockRequest)

        // Assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(customerDTO.getName())));

        // Capture JSON response as an MvcResult
        MvcResult result = resultActions.andReturn();

        // Convert the JSON response into a string
        String jsonResponse = result.getResponse().getContentAsString();

        // Log the response
        log.info(jsonResponse);
    }
}