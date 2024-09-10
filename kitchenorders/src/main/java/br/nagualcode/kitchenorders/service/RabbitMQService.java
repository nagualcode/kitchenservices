package br.nagualcode.kitchenorders.service;

import br.nagualcode.kitchenorders.dto.CustomerDTO;
import br.nagualcode.kitchenorders.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderMessage(Order order, CustomerDTO customer) {
        // Envio de mensagem ao RabbitMQ
        String exchange = "order.exchange";
        String routingKey = "order.created";
        OrderCustomerMessage message = new OrderCustomerMessage(order, customer);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    static class OrderCustomerMessage {
        private Order order;
        private CustomerDTO customer;

        public OrderCustomerMessage(Order order, CustomerDTO customer) {
            this.order = order;
            this.customer = customer;
        }

        // Getters and Setters
        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public CustomerDTO getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerDTO customer) {
            this.customer = customer;
        }
    }
}
