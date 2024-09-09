package br.nagualcode.kitchencustomer.repository;

import br.nagualcode.kitchencustomer.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
