package com.appsdeveloperblog.estore.productsservice.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class ProductAggregate {
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) {
		// Validate CreateProductCommand
	}
}
