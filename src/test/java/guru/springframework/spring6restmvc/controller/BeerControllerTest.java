package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.h2.store.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    //TODO: Tests are ignoring the base URI mapping of the BeerController, I had to comment out the @RequestMapping at the class level and modify BEER_PATH_ID as a result. Fix this!!!!!

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

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Test
    void updateBeerValidation() throws Exception {
        //Arrange
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Clank")
                .quantityOnHand(99)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        MockHttpServletRequestBuilder mockUpdate = MockMvcRequestBuilders
                .put(BeerController.BEER_PATH_ID, UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO));
        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.ofNullable(beerDTO));
        //Act
        MvcResult result = mockMvc.perform(mockUpdate)
        //Assert
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)))
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    void postBeerValidation() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BeerDTO.builder()
                        .quantityOnHand(99)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .price(BigDecimal.valueOf(34))
                        .upc("asfawf")
                        .beerStyle(BeerStyle.LAGER)
                        .build()));
        //TODO: Test passes without this
//        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(BeerDTO.builder().build());
        //Act
        MvcResult result = mockMvc.perform(mockHttpServletRequestBuilder)
                //Assert
                .andExpect(jsonPath("$.beerName").doesNotExist())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();
        //Log
                log.info(result.getResponse().getContentAsString());
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerByID(any(UUID.class))).willReturn(Optional.empty());
        MockHttpServletRequestBuilder mockGet = MockMvcRequestBuilders.get(BeerController.BEER_PATH_ID, UUID.randomUUID());
        log.info("Request URI: " + mockGet.buildRequest(new MockServletContext()).getRequestURI());
        mockMvc.perform(mockGet)
                .andExpect(status().isNotFound());
    }

    @Test
    void patchById() throws Exception {
        //ARRANGE
        BeerDTO testBeerDTO = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Ratchet");
        /*MockHttpServletRequestBuilder mockPatchRequest = patch(BeerController.BEER_PATH_ID, testBeerDTO.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap));*/
        //Opt to use MockMvcRequestBuilders to create mock HTTP requests from now on
        MockHttpServletRequestBuilder mockPatchRequest = MockMvcRequestBuilders
                .patch(BeerController.BEER_PATH_ID, testBeerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap));
        log.info("Request URI: " + mockPatchRequest.buildRequest(new MockServletContext()).getRequestURI());
        //ACT
        mockMvc.perform(mockPatchRequest)
                //ASSERT
                .andExpect(status().isNoContent());
        verify(beerService).patchBeer(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertEquals(testBeerDTO.getId(), uuidArgumentCaptor.getValue());
        assertEquals(beerMap.get("beerName"), beerArgumentCaptor.getValue().getBeerName());
        //LOG
        // objectMapper in conjunction with the captor was a good choice to get better information out of the object being passed
        log.info(objectMapper.writeValueAsString(beerArgumentCaptor.getValue()));
    }

    @Test
    void deleteById() throws Exception {
        //ARRANGE
        BeerDTO testBeerDTO = beerServiceImpl.listBeers().get(0);
        MockHttpServletRequestBuilder mockDeleteRequest = MockMvcRequestBuilders.delete(BeerController.BEER_PATH_ID, testBeerDTO.getId());
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        given(beerService.deleteById(any())).willReturn(true);
        //ACT
        // A delete request has no content, so I am not going to add content-type headers. I'm also not expecting a response, so I'll also omit accept headers
        mockMvc.perform(mockDeleteRequest)
        //ASSERT
                .andExpect(status().isNoContent());
        // Verifies that the mock object's (beerService) deleteById() method was called
        // Additionally, captures the UUID being passed into deleteById() and returns it. Which means that verify() will always pass as long as deleteById() is indeed called, however, this is a necessary step to capturing that value and then asserting against it.
        verify(beerService).deleteById(uuidArgumentCaptor.capture());
        UUID capturedUUID = uuidArgumentCaptor.getValue();
        assertEquals(testBeerDTO.getId(), capturedUUID);
        //LOG
        // Actually logs nothing useful
        log.info(objectMapper.writeValueAsString(mockDeleteRequest));
    }

    @Test
    void updateById() throws Exception {
        //ARRANGE
        BeerDTO testBeerDTO = beerServiceImpl.listBeers().get(0);
        String requestBody = objectMapper.writeValueAsString(testBeerDTO);
        MockHttpServletRequestBuilder mockPutRequest = MockMvcRequestBuilders.put(BeerController.BEER_PATH_ID, testBeerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(testBeerDTO));
        //ACT
        ResultActions resultActions = mockMvc.perform(mockPutRequest)
        //ASSERT
                .andExpect(status().isNoContent());
        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
        //LOG
        log.info(requestBody);
    }

    @Test
    void handlePost() throws Exception {
        //ARRANGE
        BeerDTO testBeerDTO = beerServiceImpl.listBeers().get(0);
        testBeerDTO.setId(null);
        testBeerDTO.setVersion(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));
        MockHttpServletRequestBuilder mockPostRequest = MockMvcRequestBuilders.post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeerDTO));
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
        ResultActions resultActions = mockMvc.perform(get(BeerController.BEER_PATH)
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
        BeerDTO testBeerDTO = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerByID(any(UUID.class))).willReturn(Optional.of(testBeerDTO));

        MockHttpServletRequestBuilder mockRequest = get(BeerController.BEER_PATH_ID,
                testBeerDTO.getId().toString())
                .accept(MediaType.APPLICATION_JSON);

        ResultActions resultActions = mockMvc.perform(mockRequest)
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())))
                        .andExpect(jsonPath("$.beerName", is(testBeerDTO.getBeerName())));

        log.info(resultActions.andReturn().getResponse().getContentAsString());
    }
}