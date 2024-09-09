package br.nagualcode.kitchencustomer.controller;

import br.nagualcode.kitchencustomer.model.Customer;
import br.nagualcode.kitchencustomer.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return Mono.fromCallable(() -> customerRepository.save(customer));
    }

    @GetMapping
    public Flux<Customer> getAllCustomers() {
        return Flux.defer(() -> Flux.fromIterable(customerRepository.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable Long id) {
        return Mono.fromCallable(() -> customerRepository.findById(id).orElse(null));
    }
}
