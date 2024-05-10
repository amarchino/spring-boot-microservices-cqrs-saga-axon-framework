package com.appsdeveloperblog.estore.ordersservice.saga;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.appsdeveloperblog.estore.core.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.PaymentProcessedEvent;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.core.model.User;
import com.appsdeveloperblog.estore.core.query.FetchUserPaymentDetailsQuery;
import com.appsdeveloperblog.estore.ordersservice.command.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderCreatedEvent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Saga
@NoArgsConstructor
@Slf4j
public class OrderSaga {
	@Autowired private transient CommandGateway commandGateway;
	@Autowired private transient QueryGateway queryGateway;

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
		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
		User userPaymentDetails = null;
		try {
			userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		} catch(Exception ex) {
			log.error("{}", ex.getMessage());
			// Start compensating transaction
			return;
		}
		if(userPaymentDetails == null) {
			// Start compensating transaction
			return;
		}
		log.info("Successfully fetched user payment details for user {}", userPaymentDetails.getFirstName());
		
		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
				.orderId(productReservedEvent.getOrderId())
				.paymentDetails(userPaymentDetails.getPaymentDetails())
				.paymentId(UUID.randomUUID().toString())
				.build();
		String result = null;
		try {
			result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
		}catch (Exception ex) {
			log.error("{}", ex.getMessage());
			// Start compensating transaction
			return;
		}
		if(result == null) {
			// Start compensating transaction
			return;
		}
		log.info("Successfully send process payment command for id {}", processPaymentCommand.getPaymentId());
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		// Send an ApproveOrderCommand
		ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		commandGateway.send(approveOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		log.info("Order is approved. Order saga is complete for orderId {}", orderApprovedEvent.getOrderId());
//		SagaLifecycle.end();
	}
}
