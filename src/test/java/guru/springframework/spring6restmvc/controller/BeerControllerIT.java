package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

// AssertJ specialized assertions. More intuitive.
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
// Invoking this annotation to set the order in which the tests are executed. This will require the use of the @Transactional and @Rollback annotations given that the database is deleted on the first test, when testing the entire fixture all together.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void testListBeersByName() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder mockGetBeers = MockMvcRequestBuilders.get(BeerController.BEER_PATH).queryParam("beerName", "IPA");
        //Act
        mockMvc.perform(mockGetBeers)
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @Test
    void deleteByIdUP() {
        assertThrows(NotFoundException.class, () -> beerController.deleteById(UUID.randomUUID()));
    }

    @Test
    void deleteByIdHP() {
        UUID uuid = beerRepository.findAll().get(0).getId();
        Optional<Beer> beerBefore = beerRepository.findById(uuid);
        beerBefore.ifPresent(beer -> log.info(beer.toString()));

        beerController.deleteById(uuid);

        Optional<Beer> beerAfter = beerRepository.findById(uuid);
        beerAfter.ifPresentOrElse(
                beer -> log.info(beer.toString()),
                () -> log.info("Empty beer after deletion")
        );

        assertThat(beerRepository.findById(uuid)).isEmpty();
    }

    @Test
    void updateBeerUP() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Test
    void updateBeerHP() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Transactional
    @Test
    void saveNewBeerTest() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<BeerDTO> responseEntity = beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Test //UP = Unhappy Path
    void getBeerByIdUP() {
        //Assert
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());});
    }

    @Test //HP = Happy Path
    void getBeerByIdHP() {
        //Arrange
        UUID beerUUID = beerRepository.findAll().get(0).getId();
        //Assert
        assertNotNull(beerController.getBeerById(beerUUID));
    }

    @Test
    void listBeers() {
        //Arrange
        List<BeerDTO> beerDTOS = beerController.listBeers(null);
        log.info(beerDTOS.toString());
        //Assert
        assertThat(beerDTOS.size()).isEqualTo(2413);
    }

    @Transactional
    @Test
    @Order(1)
    void listEmptyBeers() {
        beerRepository.deleteAll();
        assertThat(beerController.listBeers(null).size()).isEqualTo(0);
    }
}