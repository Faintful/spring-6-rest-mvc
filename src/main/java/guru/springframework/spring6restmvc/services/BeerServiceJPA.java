package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        List<Beer> listOfBeers;
        if (StringUtils.hasText(beerName) && StringUtils.hasText(String.valueOf(beerStyle))) {
            listOfBeers = listBeersByNameAndStyle(beerName, beerStyle);
        } else if (StringUtils.hasText(beerName)) {
            listOfBeers = listBeersByName(beerName);
        } else if (StringUtils.hasText(String.valueOf(beerStyle))) {
            listOfBeers = listBeersByStyle(beerStyle);
        } else {
            listOfBeers = beerRepository.findAll();
        }
        return listOfBeers.stream().map(beerMapper::beerToBeerDto).collect(Collectors.toList());
    }

    private List<Beer> listBeersByStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(String.valueOf(beerStyle));
    }

    private List<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle) {
        return null;
    }

    private List<Beer> listBeersByName(String beerName) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public Optional<BeerDTO> getBeerByID(UUID uuid) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(uuid).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID uuid, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(uuid).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beerDTO.getBeerName());
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            foundBeer.setUpc(beerDTO.getUpc());
            foundBeer.setPrice(beerDTO.getPrice());
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (beerRepository.existsById(uuid)) {
            beerRepository.deleteById(uuid);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<BeerDTO> patchBeer(UUID uuid, BeerDTO beerDTO) {
        return Optional.empty();
    }
}
