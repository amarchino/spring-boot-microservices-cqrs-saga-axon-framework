package com.appsdeveloperblog.estore.productsservice.query.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {

	@GetMapping
	public List<ProductRestModel> getProducts() {
		return List.of();
	}
}
