package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        beerMap = new HashMap<>();

        BeerDTO beerDTO1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beerDTO1.getId(), beerDTO1);
        beerMap.put(beerDTO2.getId(), beerDTO2);
        beerMap.put(beerDTO3.getId(), beerDTO3);
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerByID(UUID uuid) {
        log.debug("Get BeerDTO by Id - in service. Id: " + uuid.toString());
        return Optional.of(beerMap.get(uuid));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .version(beerDTO.getVersion())
                .build();
        beerMap.put(savedBeerDTO.getId(), savedBeerDTO);
        return savedBeerDTO;
    }

    @Override
    public void updateBeer(UUID uuid, BeerDTO beerDTO) {
        BeerDTO updatedBeerDTO = beerMap.get(uuid);
        updatedBeerDTO.setBeerName(beerDTO.getBeerName());
        updatedBeerDTO.setPrice(beerDTO.getPrice());
        updatedBeerDTO.setUpc(beerDTO.getUpc());
        updatedBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        beerMap.put(uuid, updatedBeerDTO);
    }

    @Override
    public void deleteById(UUID uuid) {
        beerMap.remove(uuid);
    }

    @Override
    public Optional<BeerDTO> patchBeer(UUID uuid, BeerDTO beerDTO) {
        BeerDTO patchedBeerDTO = beerMap.get(uuid);
        if(beerDTO.getBeerName() != null) {
            patchedBeerDTO.setBeerName(beerDTO.getBeerName());
        }
        if (beerDTO.getBeerStyle() != null) {
            patchedBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        }
        if (beerDTO.getPrice() != null) {
            patchedBeerDTO.setPrice(beerDTO.getPrice());
        }

        if (beerDTO.getQuantityOnHand() != null){
            patchedBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        if (beerDTO.getId() != null) {
            patchedBeerDTO.setUpc(beerDTO.getUpc());
        }
        return Optional.of(patchedBeerDTO);
    }
}
