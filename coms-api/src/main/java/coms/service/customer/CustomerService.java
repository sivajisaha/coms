package coms.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import coms.model.Customer;
import coms.model.CustomerRepository;

@Component
public class CustomerService {
	
	@Autowired
	public CustomerRepository repository;
	
	public Iterable<Customer> getallcustomers() {
		System.out.println("CustomerService.getallcustomers()");
		return repository.findAll();
	}
	
	public String seed() {
		repository.save(new Customer(1, "A1001","Sivaji","Saha", "T"));
		repository.save(new Customer(2, "B1001","Snehashish","Chanda", "T"));
		repository.save(new Customer(3, "C1001","Sibendu","Das", "P"));
		return "success";
	}
	
	public Customer getCustomer(Long custId) {
		return repository.findById(custId).get();
	}
}
