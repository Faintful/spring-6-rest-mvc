package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCsvService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerRepository();
        loadCustomerRepository();
        loadCsvData();
    }

    private void loadCsvData() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCSVRecord> list = beerCsvService.convertCSV(file);
        list.forEach((record) -> {
            BeerStyle beerStyle = switch (record.getStyle()) {
                case "American Pale Lager" -> BeerStyle.LAGER;
                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                        BeerStyle.ALE;
                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                case "American Porter" -> BeerStyle.PORTER;
                case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                case "English Pale Ale" -> BeerStyle.PALE_ALE;
                default -> BeerStyle.PILSNER;
            };
            beerRepository.save(Beer.builder()
                    .beerName(StringUtils.abbreviate(record.getBeer(), 50))
                    .beerStyle(beerStyle)
                    .price(BigDecimal.TEN)
                    .upc(record.getRow().toString())
                    .quantityOnHand(record.getCount())
                    .build());
        });
    }

    private void loadBeerRepository() {
        if (beerRepository.count() == 0) {
            Beer beerDTO1 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("12356")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beerDTO2 = Beer.builder()
                    .beerName("Crank")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("12356222")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(392)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beerDTO3 = Beer.builder()
                    .beerName("Sunshine City")
                    .beerStyle(BeerStyle.IPA)
                    .upc("12356")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(144)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.saveAll(Arrays.asList(
                    beerDTO1,
                    beerDTO2,
                    beerDTO3
            ));
        }
    }

    private void loadCustomerRepository() {
        if (customerRepository.count() == 0) {
            Customer customerDTO1 = Customer.builder()
                    .name("CustomerDTO 1")
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Customer customerDTO2 = Customer.builder()
                    .name("CustomerDTO 2")
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Customer customerDTO3 = Customer.builder()
                    .name("CustomerDTO 3")
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(
                    customerDTO1,
                    customerDTO2,
                    customerDTO3
                    ));
        }
    }
}
