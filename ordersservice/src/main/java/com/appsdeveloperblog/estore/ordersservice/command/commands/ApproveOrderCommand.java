package com.appsdeveloperblog.estore.ordersservice.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class ApproveOrderCommand {
	@TargetAggregateIdentifier
	private final String orderId;
}
