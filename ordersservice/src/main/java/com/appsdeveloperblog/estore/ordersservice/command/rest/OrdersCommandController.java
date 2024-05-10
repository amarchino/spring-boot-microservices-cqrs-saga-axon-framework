package com.appsdeveloperblog.estore.ordersservice.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.ordersservice.command.commands.CreateOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrdersCommandController {
	private final CommandGateway commandGateway;

	@PostMapping
	public String createOrder(@RequestBody @Valid CreateOrderRestModel createOrderRestModel) {
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
			.orderId(UUID.randomUUID().toString())
			.userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
			.productId(createOrderRestModel.getProductId())
			.quantity(createOrderRestModel.getQuantity())
			.addressId(createOrderRestModel.getAddressId())
			.orderStatus(OrderStatus.CREATED)
			.build();
			
		String returnValue = commandGateway.sendAndWait(createOrderCommand);
		return returnValue;
	}
}
