package com.appsdeveloperblog.estore.paymentsservice.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.appsdeveloperblog.estore.core.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.estore.core.events.PaymentProcessedEvent;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {
	@AggregateIdentifier
	private String paymentId;
	private String orderId;

	@CommandHandler
	public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) throws Exception {
		if(processPaymentCommand.getOrderId() == null) {
			throw new IllegalArgumentException("Order id is a required field");
		}
		if(processPaymentCommand.getPaymentId() == null) {
			throw new IllegalArgumentException("Payment id is a required field");
		}
		if(processPaymentCommand.getPaymentDetails() == null) {
			throw new IllegalArgumentException("Payment details is a required field");
		}
		PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
				.orderId(processPaymentCommand.getOrderId())
				.paymentId(processPaymentCommand.getPaymentId())
				.build();
		AggregateLifecycle.apply(paymentProcessedEvent);
	}

	@EventSourcingHandler
	public void on(PaymentProcessedEvent paymentProcessedEvent) {
		this.orderId = paymentProcessedEvent.getOrderId();
		this.paymentId = paymentProcessedEvent.getPaymentId();
	}
}
