package coms.block.customerapi;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CustomerApiController {
	
	@Autowired
	public CustomerRepository repository;
	
	@RequestMapping("/hello")
	public String index() {
		return new Date() + "\n";
	}
	@CrossOrigin
	@GetMapping("/getallcustomers")
	public Iterable<Customer> getallcustomers() {
		return repository.findAll();
	}
	
	@CrossOrigin
	@GetMapping("/getcustomerbyid/{custid}")
	public Customer getcustomerbyid(@PathVariable Long custId) {
		return repository.findById(custId).get();
	}
	
//	@CrossOrigin
//	@PostMapping("/addcustomer")
//    public String addCustomer(@RequestBody Customer cust) {
//    	dbaccess.addCustomer(cust);
//    	return "{\"status\":\"success\"}";
//    }
}
