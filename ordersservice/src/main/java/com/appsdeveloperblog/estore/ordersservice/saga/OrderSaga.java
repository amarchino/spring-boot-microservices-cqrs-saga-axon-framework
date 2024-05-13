package com.appsdeveloperblog.estore.ordersservice.saga;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.appsdeveloperblog.estore.core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.core.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.PaymentProcessedEvent;
import com.appsdeveloperblog.estore.core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.core.model.User;
import com.appsdeveloperblog.estore.core.query.FetchUserPaymentDetailsQuery;
import com.appsdeveloperblog.estore.ordersservice.command.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.command.commands.RejectOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderRejectedEvent;
import com.appsdeveloperblog.estore.ordersservice.model.OrderSummary;
import com.appsdeveloperblog.estore.ordersservice.query.FindOrderQuery;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Saga
@NoArgsConstructor
@Slf4j
public class OrderSaga {
	private static final String PAYMENT_PROCESSING_TIMEOUT_DEADLINE = "payment-processing-timeout-deadline";
	@Autowired private transient CommandGateway commandGateway;
	@Autowired private transient QueryGateway queryGateway;
	@Autowired private transient DeadlineManager deadlineManager;
	@Autowired private transient QueryUpdateEmitter queryUpdateEmitter;

	private String scheduleId;

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
				RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderCreatedEvent.getOrderId(), exception.getMessage());
				commandGateway.send(rejectOrderCommand);
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
			cancelProductReservation(productReservedEvent, ex.getMessage());
			return;
		}
		if(userPaymentDetails == null) {
			// Start compensating transaction
			cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
			return;
		}
		log.info("Successfully fetched user payment details for user {}", userPaymentDetails.getFirstName());
		
		scheduleId = deadlineManager.schedule(Duration.of(2, ChronoUnit.MINUTES), PAYMENT_PROCESSING_TIMEOUT_DEADLINE, productReservedEvent);
		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
				.orderId(productReservedEvent.getOrderId())
				.paymentDetails(userPaymentDetails.getPaymentDetails())
				.paymentId(UUID.randomUUID().toString())
				.build();
		String result = null;
		try {
			result = commandGateway.sendAndWait(processPaymentCommand);
		}catch (Exception ex) {
			log.error("{}", ex.getMessage());
			// Start compensating transaction
			cancelProductReservation(productReservedEvent, ex.getMessage());
			return;
		}
		if(result == null) {
			// Start compensating transaction
			cancelProductReservation(productReservedEvent, "Could not process user payment with provided payment details");
			return;
		}
		log.info("Successfully send process payment command for id {}", processPaymentCommand.getPaymentId());
	}
	private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
		cancelDeadline();
		CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
				.orderId(productReservedEvent.getOrderId())
				.productId(productReservedEvent.getProductId())
				.quantity(productReservedEvent.getQuantity())
				.userId(productReservedEvent.getUserId())
				.reason(reason)
				.build();
		commandGateway.send(cancelProductReservationCommand);
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		cancelDeadline();
		// Send an ApproveOrderCommand
		ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		commandGateway.send(approveOrderCommand);
	}

	private void cancelDeadline() {
		if(scheduleId != null) {
			deadlineManager.cancelSchedule(PAYMENT_PROCESSING_TIMEOUT_DEADLINE, scheduleId);
			scheduleId = null;
		}
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		log.info("Order is approved. Order saga is complete for orderId {}", orderApprovedEvent.getOrderId());
//		SagaLifecycle.end();
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, new OrderSummary(orderApprovedEvent.getOrderId(), orderApprovedEvent.getOrderStatus(), ""));
	}
	
	// Compensating
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
		// Create and send RejectOrderCommand
		RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(), productReservationCancelledEvent.getReason());
		commandGateway.send(rejectOrderCommand);
	}
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		log.info("Successfully rejected order with id {} with reason: {}", orderRejectedEvent.getOrderId(), orderRejectedEvent.getReason());
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, new OrderSummary(orderRejectedEvent.getOrderId(), orderRejectedEvent.getOrderStatus(), orderRejectedEvent.getReason()));
	}
	
	// Deadline
	@DeadlineHandler(deadlineName = PAYMENT_PROCESSING_TIMEOUT_DEADLINE)
	public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
		log.info("Payment processing deadline took place. Sendind a compensating command to cancel the product reservation");
		cancelProductReservation(productReservedEvent, "Payment timeout");
	}
}
