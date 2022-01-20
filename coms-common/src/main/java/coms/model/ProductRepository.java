package coms.model;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

	//List<Customer> findByLast_Name(String lastName);

	Product findById(long id);
}
