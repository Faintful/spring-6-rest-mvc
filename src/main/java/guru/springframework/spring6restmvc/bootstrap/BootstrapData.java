package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component // Needed for integration testing otherwise H2 will not contain any data
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerRepository();
        loadCustomerRepository();
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
