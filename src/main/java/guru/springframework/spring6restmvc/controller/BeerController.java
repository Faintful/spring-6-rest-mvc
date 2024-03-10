package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
//@RequestMapping(BeerController.BEER_PATH) // Equivalent to: @RequestMapping(path = "/api/v1/beer")
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}"; // Should be just "/{beerId}"
    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchById(@PathVariable("beerId") UUID uuid, @RequestBody BeerDTO beerDTO) {
        Optional<BeerDTO> patchedBeer = beerService.patchBeer(uuid, beerDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(patchedBeer);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID uuid) {
        if (!beerService.deleteById(uuid)) {
            throw new NotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId") UUID uuid, @RequestBody @Validated BeerDTO beerDTO) {
        if (beerService.updateBeer(uuid, beerDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(BEER_PATH) // Equivalent to: @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BeerDTO> handlePost(@RequestBody @Validated BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beerDTO/" + savedBeerDTO.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(savedBeerDTO); // Allows higher cutomizability in HTTP response as opposed to @ResponseBody
        // return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = BEER_PATH)
    public List<BeerDTO> listBeers(@RequestParam(required = false) String beerName, @RequestParam(required = false) String beerStyle) {
        return beerService.listBeers(beerName, beerStyle);
    }

    @RequestMapping(method = RequestMethod.GET, path = BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get BeerDTO By ID - In Controller");
        return beerService.getBeerByID(beerId).orElseThrow(NotFoundException::new);
    }

}
