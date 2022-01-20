package coms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coms.model.Customer;
import coms.model.CustomerRepository;
import coms.service.customer.CustomerService;


@RestController
public class CustomerApiController {
	
	@Autowired
	public CustomerService customerService;
	
	@GetMapping("/hello")
	public String index() {
		System.out.println("Inside hello");
		return new Date() + "\n";
	}
	
	@GetMapping("/seedcustomer")
	public String seedData() {
		return customerService.seed();
	}
	
	@GetMapping("/getallcustomers")
	public Iterable<Customer> getallcustomers() {
		return customerService.getallcustomers();
	}
	
	@GetMapping("/getcustomerbyid/{custid}")
	public Customer getcustomerbyid(@PathVariable Long custId) {
		return customerService.getCustomer(custId);
	}
	
//	@CrossOrigin
//	@PostMapping("/addcustomer")
//    public String addCustomer(@RequestBody Customer cust) {
//    	dbaccess.addCustomer(cust);
//    	return "{\"status\":\"success\"}";
//    }
}
