package com.appsdeveloperblog.estore.ordersservice.query.rest;

import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;

import lombok.Data;

@Data
public class OrderRestModel {

	private String orderId;
	private String productId;
	private String userId;
	private Integer quantity;
	private String addressId;
	private OrderStatus orderStatus;
}
