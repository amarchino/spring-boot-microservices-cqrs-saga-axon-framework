package com.appsdeveloperblog.estore.productsservice.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productsservice.data.ProductLookupRepository;
import com.appsdeveloperblog.estore.productsservice.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {
	private final ProductLookupRepository productLookupRepository;

	@EventHandler
	public void on(ProductCreatedEvent event) {
		ProductLookupEntity entity = new ProductLookupEntity(event.getProductId(), event.getTitle());
		productLookupRepository.save(entity);
	}

	@ResetHandler
	public void reset() {
		productLookupRepository.deleteAll();
	}
}
