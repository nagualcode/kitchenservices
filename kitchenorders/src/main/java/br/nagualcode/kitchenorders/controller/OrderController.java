package br.nagualcode.kitchenorders.controller;

import br.nagualcode.kitchenorders.model.Order;
import br.nagualcode.kitchenorders.model.Order.PaymentStatus;
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

    /**
     * Cria uma nova ordem e associa ao cliente, publica no RabbitMQ.
     */
    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return customerService.getCustomerById(order.getCustomerId())
            .flatMap(customer -> {
                // Garantir que o status da ordem seja "Aguardando Pagamento"
                order.setPaymentStatus(PaymentStatus.AGUARDANDO_PAGAMENTO);

                // Salvar a ordem no banco
                Order savedOrder = orderRepository.save(order);

                // Adicionar o ID da ordem na lista de ordens do cliente
                customer.getOrders().add(savedOrder.getId());
                customerService.updateCustomerOrders(customer);

                // Publicar mensagem no RabbitMQ
                rabbitMQService.sendOrderMessage(savedOrder, customer);

                return Mono.just(savedOrder);
            })
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado")));
    }

    /**
     * Deleta uma ordem pelo ID. Não remove o ID da lista de ordens do cliente.
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable Long id) {
        return Mono.fromRunnable(() -> {
            // Deletar a ordem do banco
            orderRepository.deleteById(id);
        });
    }

    /**
     * Busca todas as ordens.
     */
    @GetMapping
    public Flux<Order> getAllOrders() {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()));
    }

    /**
     * Busca uma ordem pelo ID.
     */
    @GetMapping("/{id}")
    public Mono<Order> getOrderById(@PathVariable Long id) {
        return Mono.fromCallable(() -> orderRepository.findById(id).orElse(null));
    }
}
