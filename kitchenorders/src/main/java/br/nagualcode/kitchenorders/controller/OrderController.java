package br.nagualcode.kitchenorders.controller;

import br.nagualcode.kitchenorders.dto.OrderDTO;
import br.nagualcode.kitchenorders.model.Order;
import br.nagualcode.kitchenorders.repository.OrderRepository;
import br.nagualcode.kitchenorders.service.CustomerService;
import br.nagualcode.kitchenorders.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final RabbitMQService rabbitMQService;

    @Autowired
    public OrderController(OrderRepository orderRepository, CustomerService customerService, RabbitMQService rabbitMQService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.rabbitMQService = rabbitMQService;
    }

    @PostMapping
    public Mono<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        // Validação do valor e ID do cliente
        if (orderDTO.getValor() == null || orderDTO.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("O valor do pedido deve ser maior que zero"));
        }
        if (orderDTO.getCustomerId() == null || orderDTO.getCustomerId() <= 0) {
            return Mono.error(new IllegalArgumentException("ID do cliente inválido"));
        }

        return customerService.getCustomerById(orderDTO.getCustomerId())
            .switchIfEmpty(Mono.error(new RuntimeException("Cliente não encontrado")))
            .flatMap(customerDTO -> {
                Order newOrder = new Order(orderDTO.getCustomerId(), "Aguardando Pagamento", orderDTO.getValor());

                return Mono.fromCallable(() -> orderRepository.save(newOrder))
                    .onErrorResume(e -> {
                        // Log do erro e mensagem de exceção apropriada
                        return Mono.error(new RuntimeException("Erro ao salvar o pedido no banco de dados", e));
                    })
                    .flatMap(savedOrder -> {
                        customerDTO.getOrders().add(savedOrder.getId());
                        customerService.updateCustomerOrders(customerDTO);
                        rabbitMQService.sendOrderMessage(savedOrder, customerDTO);

                        return Mono.just(new OrderDTO(savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getStatus(), savedOrder.getValor()));
                    });
            })
            .onErrorResume(e -> {
                // Log do erro e mensagem apropriada no caso de falha ao obter o cliente
                return Mono.error(new RuntimeException("Erro ao buscar cliente", e));
            });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return Mono.error(new IllegalArgumentException("ID do pedido inválido"));
        }
        return Mono.fromRunnable(() -> orderRepository.deleteById(id));
    }

    @GetMapping
    public Flux<OrderDTO> getAllOrders() {
        return Flux.fromIterable(orderRepository.findAll())
            .map(order -> new OrderDTO(order.getId(), order.getCustomerId(), order.getStatus(), order.getValor()));
    }
}
