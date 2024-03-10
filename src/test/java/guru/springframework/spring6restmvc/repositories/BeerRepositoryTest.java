package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void findAllByBeerNameIsLike() {
        List<Beer> listOfBeers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertThat(listOfBeers.size()).isEqualTo(336);
    }

    @Transactional
    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer testBeer = Beer.builder()
                    .beerName("fkaejfgkeasjgkaejakejkajegkajsegkjasekgaksegjaegasegjaejgaseguaseg")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("234234234234")
                    .price(new BigDecimal("11.99"))
                    .build();
            log.info("Before persisting: " + testBeer.toString());
            Beer beerSaved = beerRepository.save(testBeer);
            log.info("After persisting: " + beerSaved.toString());
            beerRepository.flush();
        });
    }

    @Transactional
    @Test
    void saveBeer() {
        Beer testBeer = Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("234234234234")
                .price(new BigDecimal("11.99"))
                .build();
        log.info("Before persisting: " + testBeer.toString());
        Beer beerSaved = beerRepository.save(testBeer);
        log.info("After persisting: " + beerSaved.toString());
        beerRepository.flush();
        assertNotNull(beerRepository.getReferenceById(testBeer.getId()));
    }

}