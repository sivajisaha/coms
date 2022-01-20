package coms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coms.model.*;
import coms.service.customer.ProductService;

@RestController
@RequestMapping("/products")
public class ProductApiController {
	
	@Autowired
	public ProductService productService;
	
	@GetMapping("/")
	public Iterable<Product> getProducts() {
		System.out.println("Inside getProducts");
		Iterable<Product> products = null;
		try{
			products = productService.getProducts();
			System.out.println(products);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception :: "+e.getMessage());
		}
		return products;
	}
	
	@GetMapping("/seed")
	public String seed() {
		return productService.seed();
	}
}
