package com.appsdeveloperblog.estore.productsservice.rest;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateProductRestModel {
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
