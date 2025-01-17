package com.appsdeveloperblog.estore.ordersservice.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class RejectOrderCommand {
	@TargetAggregateIdentifier
	private final String orderId;
	private final String reason;
}
