package br.nagualcode.kitchenorders.service;

import br.nagualcode.kitchenorders.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final WebClient webClient;

    public CustomerService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://customer-service-url/customers").build();
    }

    public Mono<CustomerDTO> getCustomerById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(CustomerDTO.class);
    }

    public Mono<Void> updateCustomerOrders(CustomerDTO customer) {
        return webClient.put()
                .uri("/{id}", customer.getId())
                .bodyValue(customer)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
