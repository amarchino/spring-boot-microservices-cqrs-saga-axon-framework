package com.appsdeveloperblog.estore.ordersservice.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import com.appsdeveloperblog.estore.ordersservice.core.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;

@Saga
@RequiredArgsConstructor
public class OrderSaga {
	private final transient CommandGateway commandGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		
	}
}
