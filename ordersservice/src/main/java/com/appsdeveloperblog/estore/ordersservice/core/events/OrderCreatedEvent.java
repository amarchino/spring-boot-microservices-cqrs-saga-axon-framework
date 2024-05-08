package com.appsdeveloperblog.estore.ordersservice.core.events;

import com.appsdeveloperblog.estore.ordersservice.core.OrderStatus;

import lombok.Data;

@Data
public class OrderCreatedEvent {
	private String orderId;
	private String productId;
	private String userId;
	private Integer quantity;
	private String addressId;
	private OrderStatus orderStatus;
}
