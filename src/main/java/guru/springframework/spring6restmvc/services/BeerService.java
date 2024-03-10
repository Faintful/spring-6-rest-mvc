package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle);

    Optional<BeerDTO> getBeerByID(UUID uuid);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeer(UUID uuid, BeerDTO beerDTO);

    boolean deleteById(UUID uuid);

    Optional<BeerDTO> patchBeer(UUID uuid, BeerDTO beerDTO);
}
