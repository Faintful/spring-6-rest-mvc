package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(CustomerController.CUSTOMER_PATH)
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = "/{customerId}";
    private final CustomerService customerService;

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("customerId") UUID uuid, @RequestBody CustomerDTO customerDTO) {
        customerService.updateById(uuid, customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Thing", customerDTO.getId().toString());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).body("Nice");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CustomerDTO> handlePost(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomerDTO = customerService.saveNewCustomer(customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customerDTO/" + savedCustomerDTO.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @RequestMapping(path = CUSTOMER_PATH_ID, method = RequestMethod.GET)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID uuid) {
        return customerService.getCustomerById(uuid).orElseThrow(NotFoundException::new);
    }
}
