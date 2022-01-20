package coms.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import coms.model.Product;
import coms.model.ProductRepository;

@Component
public class ProductService {
	
	@Autowired
	public ProductRepository repository;
	
	public Iterable<Product> getProducts() {
		System.out.println("Inside getProducts");
		Iterable<Product> products = null;
		try{
			products = repository.findAll();
			System.out.println(products);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception :: "+e.getMessage());
		}
		return products;
	}
	
	public String seed() {
		repository.save(new Product(new Long(1),"PP220002","Product Code","My Product"));
		return "success";
	}

}
