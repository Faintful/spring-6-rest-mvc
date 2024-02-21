package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void saveBeer() {
        Beer testBeer = Beer.builder().beerName("Ratchet").build();
        log.info("Before persisting: " + testBeer.toString());
        Beer beerSaved = beerRepository.save(testBeer);
        log.info("After persisting: " + beerSaved.toString());
        assertNotNull(beerRepository.getReferenceById(testBeer.getId()));
    }

}