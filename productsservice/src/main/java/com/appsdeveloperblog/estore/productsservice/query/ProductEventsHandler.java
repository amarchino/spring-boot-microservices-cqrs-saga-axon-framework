package com.appsdeveloperblog.estore.productsservice.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;

@Component
public class ProductEventsHandler {

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		
	}
}
