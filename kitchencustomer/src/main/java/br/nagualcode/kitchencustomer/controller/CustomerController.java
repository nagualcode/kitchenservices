package br.nagualcode.kitchencustomer.controller;

import br.nagualcode.kitchencustomer.model.Customer;
import br.nagualcode.kitchencustomer.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Criar um novo cliente
    @PostMapping
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return Mono.fromCallable(() -> customerRepository.save(customer));
    }

    // Obter todos os clientes
    @GetMapping
    public Flux<Customer> getAllCustomers() {
        return Flux.fromIterable(customerRepository.findAll());
    }


    // Obter um cliente por ID
    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable Long id) {
        return Mono.fromCallable(() -> customerRepository.findById(id).orElse(null));
    }

    // Atualizar um cliente existente
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return Mono.fromCallable(() -> {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                customer.setNome(customerDetails.getNome());
                customer.setEmail(customerDetails.getEmail());
                customerRepository.save(customer);
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    // Atualizar apenas a lista de ordens de um cliente


    @PutMapping("/{id}/orders")
    @Transactional  // Adiciona suporte transacional para garantir atomicidade
    public Mono<ResponseEntity<Customer>> addOrderToCustomer(@PathVariable Long id, @RequestBody Long orderId) {
        return Mono.fromCallable(() -> {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                synchronized (customer.getOrders()) {  // Sincroniza o acesso à lista de ordens
                    customer.getOrders().add(orderId);  // Adiciona a nova ordem de forma segura
                }
                customerRepository.save(customer);
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }


    // Deletar um cliente
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Long id) {
        return Mono.fromCallable(() -> {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }
}
