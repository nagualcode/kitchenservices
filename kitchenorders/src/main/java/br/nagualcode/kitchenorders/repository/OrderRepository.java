package br.nagualcode.kitchenorders.repository;

import br.nagualcode.kitchenorders.model.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    default Order safeSave(Order order) throws DataAccessException {
        try {
            return save(order);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao salvar pedido no banco de dados", e);
        }
    }

    default void safeDeleteById(Long id) throws DataAccessException {
        try {
            deleteById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao deletar pedido do banco de dados", e);
        }
    }
}
