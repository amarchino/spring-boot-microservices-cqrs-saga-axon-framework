package com.appsdeveloperblog.estore.productsservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.productsservice.data.ProductEntity;
import com.appsdeveloperblog.estore.productsservice.data.ProductsRepository;
import com.appsdeveloperblog.estore.productsservice.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
@Slf4j
public class ProductEventsHandler {

	private final ProductsRepository productsRepository;

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		productsRepository.save(productEntity);
	}
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		productsRepository.save(productEntity);
		log.info("ProductReservedEvent is called for orderId: {} and productId: {}", productReservedEvent.getOrderId(), productReservedEvent.getProductId());
	}

	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) {
		// Log error message
	}
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}
}
