package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
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
    public ResponseEntity updateById(@PathVariable("customerId") UUID uuid, @RequestBody Customer customer) {
        customerService.updateById(uuid, customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Thing", customer.getId().toString());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).body("Nice");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Customer> handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @RequestMapping(path = CUSTOMER_PATH_ID, method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID uuid) {
        return customerService.getCustomerById(uuid);
    }
}
