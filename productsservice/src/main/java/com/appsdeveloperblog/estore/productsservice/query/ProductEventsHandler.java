package com.appsdeveloperblog.estore.productsservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.ProductsRepository;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
public class ProductEventsHandler {

	private final ProductsRepository productsRepository;

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		productsRepository.save(productEntity);
	}
}
