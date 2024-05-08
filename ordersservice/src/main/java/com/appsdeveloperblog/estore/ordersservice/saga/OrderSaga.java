package com.appsdeveloperblog.estore.ordersservice.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.spring.stereotype.Saga;

import lombok.RequiredArgsConstructor;

@Saga
@RequiredArgsConstructor
public class OrderSaga {
	private final transient CommandGateway commandGateway;
}
