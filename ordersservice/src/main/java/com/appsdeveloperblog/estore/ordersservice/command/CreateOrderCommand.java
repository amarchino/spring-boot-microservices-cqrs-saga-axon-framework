package com.appsdeveloperblog.estore.ordersservice.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.appsdeveloperblog.estore.ordersservice.core.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderCommand {
	@TargetAggregateIdentifier
	public final String orderId;
	private final String userId;
	private final String productId;
	private final Integer quantity;
	private final String addressId;
	private final OrderStatus orderStatus;
}