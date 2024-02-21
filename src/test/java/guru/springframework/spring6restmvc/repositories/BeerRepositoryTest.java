package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
//@DataJpaTest
    @SpringBootTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    private BeerDTO beerDTO1;
    private BeerDTO beerDTO2;
    private BeerDTO beerDTO3;

    @BeforeEach
    void setUp() {
        beerDTO1 = BeerDTO.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerDTO2 = BeerDTO.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerDTO3 = BeerDTO.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    @Test
    void saveBeer() {
        log.info("Before persisting, Beer DTO's: " +
                "\n" + beerDTO1.toString() +
                "\n" + beerDTO2.toString() +
                "\n" + beerDTO3.toString());
        Beer beerDto1Saved = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO1));
        Beer beerDto2Saved = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO2));
        Beer beerDto3Saved = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO3));
        log.info("Before persisting, Beer entities: " +
                "\n" + beerDto1Saved.toString() +
                "\n" + beerDto2Saved.toString() +
                "\n" + beerDto3Saved.toString());
        assertNotNull(beerRepository.getReferenceById(beerDto1Saved.getId()));
        assertNotNull(beerRepository.getReferenceById(beerDto2Saved.getId()));
        assertNotNull(beerRepository.getReferenceById(beerDto3Saved.getId()));
    }

}