package com.appsdeveloperblog.estore.ordersservice.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.ordersservice.core.events.OrderCreatedEvent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Saga
@NoArgsConstructor
@Slf4j
public class OrderSaga {
	@Autowired
	private transient CommandGateway commandGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
				.productId(orderCreatedEvent.getProductId())
				.quantity(orderCreatedEvent.getQuantity())
				.orderId(orderCreatedEvent.getOrderId())
				.userId(orderCreatedEvent.getUserId())
				.build();
		log.info("OrderCreatedEvent handled for orderId: {} and productId: {}", orderCreatedEvent.getOrderId(), orderCreatedEvent.getProductId());
		commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
			if(commandResultMessage.isExceptional()) {
				Throwable exception = commandResultMessage.exceptionResult();
				log.error("Exception", exception);
				// Start a compensating transaction
			}
		});
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		log.info("ProductReservedEvent is called for orderId: {} and productId: {}", productReservedEvent.getOrderId(), productReservedEvent.getProductId());
		// Process user payment
	}
}
