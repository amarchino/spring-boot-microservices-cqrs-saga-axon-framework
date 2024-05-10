package com.appsdeveloperblog.estore.ordersservice.data;

import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class OrderEntity {

	@Id
	@Column(unique = true)
	private String orderId;
	private String productId;
	private String userId;
	private Integer quantity;
	private String addressId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
}
