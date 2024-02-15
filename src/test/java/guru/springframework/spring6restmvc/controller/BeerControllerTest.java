package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @MockBean
    BeerService beerService;

    @Test
    void deleteById() throws Exception {
        //ARRANGE
        Beer testBeer = beerServiceImpl.listBeers().get(0);
        MockHttpServletRequestBuilder mockDeleteRequest = delete("/api/v1/beer/" + testBeer.getId().toString());
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        //ACT
        // A delete request has no content, so I am not going to add content-type headers. I'm also not expecting a response, so I'll also omit accept headers
        mockMvc.perform(mockDeleteRequest)
        //ASSERT
                .andExpect(status().isNoContent());
        // Verifies that the mock object's (beerService) deleteById() method was called
        // Additionally, captures the UUID being passed into deleteById() and returns it. Which means that verify() will always pass as long as deleteById() is indeed called, however, this is a necessary step to capturing that value and then asserting against it.
        verify(beerService).deleteById(uuidArgumentCaptor.capture());
        UUID capturedUUID = uuidArgumentCaptor.getValue();
        assertEquals(testBeer.getId(), capturedUUID);
        //LOG
        // Actually logs nothing useful
        log.info(objectMapper.writeValueAsString(mockDeleteRequest));
    }

    @Test
    void updateById() throws Exception {
        //ARRANGE
        Beer testBeer = beerServiceImpl.listBeers().get(0);
        String requestBody = objectMapper.writeValueAsString(testBeer);
        MockHttpServletRequestBuilder mockPutRequest = put("/api/v1/beer/" + testBeer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        //ACT
        ResultActions resultActions = mockMvc.perform(mockPutRequest)
        //ASSERT
                .andExpect(status().isNoContent());
        verify(beerService).updateBeer(any(UUID.class), any(Beer.class));
        //LOG
        log.info(requestBody);
    }

    @Test
    void handlePost() throws Exception {
        //ARRANGE
        Beer testBeer = beerServiceImpl.listBeers().get(0);
        testBeer.setId(null);
        testBeer.setVersion(null);

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));
        MockHttpServletRequestBuilder mockPostRequest = post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer));
        //ACT
        ResultActions resultActions = mockMvc.perform(mockPostRequest)

                //ASSERT
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"));
//                .andExpect(jsonPath("$.beerName", is("Sunshine City")));

        //LOG
        MvcResult mvcResult = resultActions.andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        log.info(jsonResponse);
    }

    @Test
    void listBeers() throws Exception {
        // Configure the object that has been marked with @MockBean
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        // Make a "fake" HTTP call that will invoke the mock object and save it to a ResultActions object
        ResultActions resultActions = mockMvc.perform(get("/api/v1/beer")
                // Set the media type header of the fake HTTP request
                        .accept(MediaType.APPLICATION_JSON))
                // Then assert with andExpect() methods
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

        // Convert the ResultActions object into object MVCResult
        MvcResult result = resultActions.andReturn();

        // Convert the JSON response to a string
        String jsonResponse = result.getResponse().getContentAsString();

        // Log the JSON response with Slf4j
        log.info("JSON response: {}", jsonResponse);
    }

    @Test
    void getBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerByID(testBeer.getId())).willReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" +
                        testBeer.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                        .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
        ;
    }
}