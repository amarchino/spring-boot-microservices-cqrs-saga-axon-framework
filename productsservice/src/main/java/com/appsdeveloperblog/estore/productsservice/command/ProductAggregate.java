package com.appsdeveloperblog.estore.productsservice.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.appsdeveloperblog.estore.core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.productsservice.command.commands.CreateProductCommand;
import com.appsdeveloperblog.estore.productsservice.events.ProductCreatedEvent;

import lombok.NoArgsConstructor;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
@NoArgsConstructor
public class ProductAggregate {
	@AggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {
		// Validate CreateProductCommand
		if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price cannot be less than or equal to zero");
		}
		if(createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
		BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
		AggregateLifecycle.apply(productCreatedEvent);
	}
	@CommandHandler
	public void handle(ReserveProductCommand reserveProductCommand) throws Exception {
		if(quantity.compareTo(reserveProductCommand.getQuantity()) < 0) {
			throw new IllegalArgumentException("Insufficient number of items in stock");
		}
		ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
				.productId(reserveProductCommand.getProductId())
				.orderId(reserveProductCommand.getOrderId())
				.userId(reserveProductCommand.getUserId())
				.quantity(reserveProductCommand.getQuantity())
				.build();
		AggregateLifecycle.apply(productReservedEvent);
	}
	@CommandHandler
	public void handle(CancelProductReservationCommand cancelProductReservationCommand) throws Exception {
		ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
				.orderId(cancelProductReservationCommand.getOrderId())
				.productId(cancelProductReservationCommand.getProductId())
				.userId(cancelProductReservationCommand.getUserId())
				.quantity(cancelProductReservationCommand.getQuantity())
				.reason(cancelProductReservationCommand.getReason())
				.build();
		AggregateLifecycle.apply(productReservationCancelledEvent);
	}

	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.title = productCreatedEvent.getTitle();
		this.price = productCreatedEvent.getPrice();
		this.quantity = productCreatedEvent.getQuantity();
	}
	@EventSourcingHandler
	public void on(ProductReservedEvent productReservedEvent) {
		this.quantity -= productReservedEvent.getQuantity();
	}
	@EventSourcingHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		this.quantity += productReservationCancelledEvent.getQuantity();
	}
}
