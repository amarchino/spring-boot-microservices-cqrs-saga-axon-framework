package com.appsdeveloperblog.estore.ordersservice.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.ordersservice.core.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;

@Saga
@RequiredArgsConstructor
public class OrderSaga {
	private final transient CommandGateway commandGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
				.productId(orderCreatedEvent.getProductId())
				.quantity(orderCreatedEvent.getQuantity())
				.orderId(orderCreatedEvent.getOrderId())
				.userId(orderCreatedEvent.getUserId())
				.build();
		commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
			if(commandResultMessage.isExceptional()) {
				// Start a compensating transaction
			}
		});
	}
}
