package com.appsdeveloperblog.estore.ordersservice.errorhandling;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

public class OrdersServiceErrorHandler implements ListenerInvocationErrorHandler {

	@Override
	public void onError(Exception exception, EventMessage<?> event, EventMessageHandler eventHandler) throws Exception {
		throw exception;
	}

}
