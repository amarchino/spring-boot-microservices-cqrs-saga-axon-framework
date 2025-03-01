package com.appsdeveloperblog.estore.productsservice.data;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="products")
public class ProductEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true)
	private String productId;
	@Column(unique = true)
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
