package com.appsdeveloperblog.estore.productsservice.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class ProductAggregate {
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) {
		// Validate CreateProductCommand
		if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price cannot be less than or equal to zero");
		}
		if(createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
	}
}
