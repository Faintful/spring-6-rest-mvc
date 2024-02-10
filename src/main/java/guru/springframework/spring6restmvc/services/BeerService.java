package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();

    Beer getBeerByID(UUID uuid);

    Beer saveNewBeer(Beer beer);

    void updateBeer(UUID uuid, Beer beer);

    void deleteById(UUID uuid);

    Optional<Beer> patchBeer(UUID uuid, Beer beer);
}
