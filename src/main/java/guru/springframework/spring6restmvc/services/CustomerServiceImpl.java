package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customerMap;
    public CustomerServiceImpl() {

        customerMap = new HashMap<>();

        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("CustomerDTO 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("CustomerDTO 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("CustomerDTO 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customerDTO1.getId(), customerDTO1);
        customerMap.put(customerDTO2.getId(), customerDTO2);
        customerMap.put(customerDTO3.getId(), customerDTO3);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public CustomerDTO getCustomerById(UUID uuid) {
        return customerMap.get(uuid);
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        CustomerDTO newCustomerDTO = CustomerDTO.builder()
                .createdDate(LocalDateTime.now())
                .id(UUID.randomUUID())
                .name(customerDTO.getName())
                .updateDate(LocalDateTime.now())
                .version(customerDTO.getVersion())
                .build();
        customerMap.put(newCustomerDTO.getId(), newCustomerDTO);
        return newCustomerDTO;
    }

    @Override
    public void updateById(UUID uuid, CustomerDTO customerDTO) {
        CustomerDTO updatingCustomerDTO = customerMap.get(uuid);
        updatingCustomerDTO.setName(customerDTO.getName());
        updatingCustomerDTO.setVersion(customerDTO.getVersion());
        customerMap.put(uuid, customerDTO);
    }
}
