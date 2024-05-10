package com.appsdeveloperblog.estore.ordersservice.events;

import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {
	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
