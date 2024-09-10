package br.nagualcode.kitchenorders.service;

import br.nagualcode.kitchenorders.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final WebClient webClient;

    public CustomerService(WebClient.Builder webClientBuilder) {
        // Configura o WebClient com timeouts e logs
        this.webClient = webClientBuilder
                .baseUrl("http://kitchencustomer-service:8080")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(5))))  // Timeout de 5 segundos
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    public Mono<CustomerDTO> getCustomerById(Long customerId) {
        return this.webClient.get()
                .uri("/customers/{id}", customerId)
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError(),
                    clientResponse -> Mono.error(new RuntimeException("Erro cliente não encontrado ou requisição inválida"))
                )
                .onStatus(
                    status -> status.is5xxServerError(),
                    clientResponse -> Mono.error(new RuntimeException("Erro no servidor ao buscar cliente"))
                )
                .bodyToMono(CustomerDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    logger.error("Erro ao buscar cliente: {}", e.getMessage());
                    return Mono.error(new RuntimeException("Falha de comunicação com o serviço de clientes", e));
                });
    }

    public void updateCustomerOrders(CustomerDTO customerDTO) {
        this.webClient.put()
            .uri("/customers/{id}/orders", customerDTO.getId())
            .bodyValue(customerDTO)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                clientResponse -> Mono.error(new RuntimeException("Erro ao atualizar as ordens do cliente"))
            )
            .onStatus(
                status -> status.is5xxServerError(),
                clientResponse -> Mono.error(new RuntimeException("Erro no servidor ao atualizar cliente"))
            )
            .bodyToMono(Void.class)
            .subscribe(null, throwable -> {
                logger.error("Erro ao atualizar as ordens do cliente: {}", throwable.getMessage());
            });
    }

    // Função para registrar os detalhes da requisição
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Requisição: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    // Função para registrar os detalhes da resposta
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Resposta: Status code {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientResponse);
        });
    }
}
