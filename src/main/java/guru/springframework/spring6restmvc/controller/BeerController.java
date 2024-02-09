package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer") // Equivalent to: @RequestMapping(path = "/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @PostMapping // Equivalent to: @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBeer); // Allows higher cutomizability in HTTP response as opposed to @ResponseBody
        // return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        log.debug("Get a List of Beers - In Controller");
        return beerService.listBeers();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get Beer By ID - In Controller");
        return beerService.getBeerByID(beerId);
    }
}
