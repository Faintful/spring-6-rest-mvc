package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// TODO: What is this?
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
// Invoking this annotation to set the order in which the tests are executed. This will require the use of the @Transactional and @Rollback annotations given that the database is deleted on the first test, when testing the entire fixture all together.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

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
        List<BeerDTO> beerDTOS = beerController.listBeers();
        log.info(beerDTOS.toString());
        //Assert
        assertThat(beerDTOS.size()).isEqualTo(3);
    }

    @Transactional
    @Test
    @Order(1)
    void listEmptyBeers() {
        beerRepository.deleteAll();
        assertThat(beerController.listBeers().size()).isEqualTo(0);
    }
}