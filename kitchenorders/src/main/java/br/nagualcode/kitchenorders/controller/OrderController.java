package br.nagualcode.kitchenorders.controller;

import br.nagualcode.kitchenorders.dto.OrderDTO;

import br.nagualcode.kitchenorders.model.Order;
import br.nagualcode.kitchenorders.repository.OrderRepository;
import br.nagualcode.kitchenorders.service.CustomerService;
import br.nagualcode.kitchenorders.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return customerService.getCustomerById(orderDTO.getCustomerId())
            .flatMap(customerDTO -> {
                // Converter DTO para entidade Order
                Order newOrder = new Order(orderDTO.getCustomerId(), "Aguardando Pagamento", orderDTO.getValor());

                // Salvar a ordem no banco
                Order savedOrder = orderRepository.save(newOrder);

                // Atualizar lista de ordens no cliente
                customerDTO.getOrders().add(savedOrder.getId());
                customerService.updateCustomerOrders(customerDTO);

                // Publicar mensagem no RabbitMQ
                rabbitMQService.sendOrderMessage(savedOrder, customerDTO);

                // Converter a entidade salva de volta para DTO e retornar
                return Mono.just(new OrderDTO(savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getStatus(), savedOrder.getValor()));
            });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable Long id) {
        return Mono.fromRunnable(() -> orderRepository.deleteById(id));
    }

    @GetMapping
    public Flux<OrderDTO> getAllOrders() {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()))
            .map(order -> new OrderDTO(order.getId(), order.getCustomerId(), order.getStatus(), order.getValor()));
    }
}
