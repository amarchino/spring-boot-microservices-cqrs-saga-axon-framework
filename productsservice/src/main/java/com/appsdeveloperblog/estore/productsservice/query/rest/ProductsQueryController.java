package com.appsdeveloperblog.estore.productsservice.query.rest;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.productsservice.query.FindProductsQuery;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsQueryController {
	private final QueryGateway queryGateway;

	@GetMapping
	public List<ProductRestModel> getProducts() {
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<ProductRestModel> products = queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
		return products;
	}
}
