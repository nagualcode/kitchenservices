package br.nagualcode.kitchenorders.repository;

import br.nagualcode.kitchenorders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // As operações de CRUD padrão já estão disponíveis
}
