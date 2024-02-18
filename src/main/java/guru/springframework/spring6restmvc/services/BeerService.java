package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerByID(UUID uuid);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    void updateBeer(UUID uuid, BeerDTO beerDTO);

    void deleteById(UUID uuid);

    Optional<BeerDTO> patchBeer(UUID uuid, BeerDTO beerDTO);
}
