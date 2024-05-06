package com.appsdeveloperblog.estore.productsservice.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductLookupRepository;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {
	private final ProductLookupRepository productLookupRepository;

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
	}
}
