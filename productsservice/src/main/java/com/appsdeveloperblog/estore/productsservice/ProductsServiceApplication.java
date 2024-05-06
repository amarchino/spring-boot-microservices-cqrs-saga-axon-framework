package com.appsdeveloperblog.estore.productsservice;

import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import com.appsdeveloperblog.estore.productsservice.command.interceptors.CreateProductCommandInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Autowired
	void registerProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
		CreateProductCommandInterceptor interceptor = context.getBean(CreateProductCommandInterceptor.class);
		commandBus.registerDispatchInterceptor(interceptor);
	}

}
