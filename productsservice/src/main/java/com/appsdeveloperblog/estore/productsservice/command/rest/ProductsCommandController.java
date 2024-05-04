package com.appsdeveloperblog.estore.productsservice.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.productsservice.command.CreateProductCommand;
import com.appsdeveloperblog.estore.productsservice.rest.CreateProductRestModel;

import lombok.RequiredArgsConstructor;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductsCommandController {
	private final Environment env;
	private final CommandGateway commandGateway;

	@PostMapping
	public String createProduct(@RequestBody CreateProductRestModel createProductRestModel) {
		CreateProductCommand createProductCommand = CreateProductCommand.builder()
			.price(createProductRestModel.getPrice())
			.quantity(createProductRestModel.getQuantity())
			.title(createProductRestModel.getTitle())
			.productId(UUID.randomUUID().toString())
			.build();
		String returnValue;
		try {
			returnValue = commandGateway.sendAndWait(createProductCommand);
		} catch (Exception ex) {
			returnValue = ex.getLocalizedMessage();
		}
		
		return returnValue;
	}

//	@PutMapping
//	public String updateProduct() {
//		return "HTTP PUT Handled";
//	}
//
//	@DeleteMapping
//	public String deleteProduct() {
//		return "HTTP DELETE Handled";
//	}

}
