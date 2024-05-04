package com.appsdeveloperblog.estore.productsservice.query;

import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductsQueryHandler {
	private final ProductsRepository productsRepository;
	
}
