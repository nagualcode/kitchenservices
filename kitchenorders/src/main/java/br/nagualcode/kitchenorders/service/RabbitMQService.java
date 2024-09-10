package br.nagualcode.kitchenorders.service;

import br.nagualcode.kitchenorders.model.Order;
import br.nagualcode.kitchenorders.dto.CustomerDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderMessage(Order order, CustomerDTO customer) {
        OrderMessage orderMessage = new OrderMessage(order, customer);
        rabbitTemplate.convertAndSend(exchange, routingKey, orderMessage);
    }

    static class OrderMessage {
        private Order order;
        private CustomerDTO customer;

        public OrderMessage(Order order, CustomerDTO customer) {
            this.order = order;
            this.customer = customer;
        }

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
