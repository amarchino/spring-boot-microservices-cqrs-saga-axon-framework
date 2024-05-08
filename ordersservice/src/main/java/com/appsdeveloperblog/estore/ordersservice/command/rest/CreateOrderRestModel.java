package com.appsdeveloperblog.estore.ordersservice.command.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRestModel {
	@NotNull(message = "Product id is a required field")
	private String productId;
	@Min(value = 1, message = "Quantity cannot be lower than 1")
	@Max(value = 5, message = "Quantity cannot be larger than 5")
	private Integer quantity;
	@NotNull(message = "Address id is a required field")
	private String addressId;
}
