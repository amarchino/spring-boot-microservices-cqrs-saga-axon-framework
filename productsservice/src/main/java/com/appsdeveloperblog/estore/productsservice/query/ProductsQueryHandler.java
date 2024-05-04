package com.appsdeveloperblog.estore.productsservice.query;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductsRepository;
import com.appsdeveloperblog.estore.productsservice.query.rest.ProductRestModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductsQueryHandler {
	private final ProductsRepository productsRepository;
	
	@QueryHandler
	public List<ProductRestModel> findProducts(FindProductsQuery query) {
		return productsRepository.findAll()
			.stream()
			.map(pe -> {
				ProductRestModel prm = new ProductRestModel();
				BeanUtils.copyProperties(pe, prm);
				return prm;
			})
			.toList();
	}
}
