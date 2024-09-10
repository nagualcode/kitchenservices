package br.nagualcode.kitchenorders.service;

import br.nagualcode.kitchenorders.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final WebClient webClient;

    public CustomerService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://customers-service:8080").build();
    }

    public Mono<CustomerDTO> getCustomerById(Long customerId) {
        return this.webClient.get()
                .uri("/customers/{id}", customerId)
                .retrieve()
                .bodyToMono(CustomerDTO.class);
    }

    public void updateCustomerOrders(CustomerDTO customerDTO) {
        this.webClient.put()
                .uri("/customers/{id}/orders", customerDTO.getId())
                .bodyValue(customerDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
