package br.nagualcode.kitchencustomer.repository;

import br.nagualcode.kitchencustomer.model.Customer;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    default Customer safeSave(Customer customer) throws DataAccessException {
        try {
            return save(customer);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao salvar cliente no banco de dados", e);
        }
    }

    default void safeDeleteById(Long id) throws DataAccessException {
        try {
            deleteById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao deletar cliente do banco de dados", e);
        }
    }
}
