package com.appsdeveloperblog.estore.productsservice;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.appsdeveloperblog.estore.core.config.AxonConfig;
import com.appsdeveloperblog.estore.productsservice.command.interceptors.CreateProductCommandInterceptor;
import com.appsdeveloperblog.estore.productsservice.errorhandling.ProductsServiceErrorHandler;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ AxonConfig.class })
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Autowired
	void registerProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
		CreateProductCommandInterceptor interceptor = context.getBean(CreateProductCommandInterceptor.class);
		commandBus.registerDispatchInterceptor(interceptor);
	}

	@Autowired
	void configure(EventProcessingConfigurer configurer) {
		configurer.registerListenerInvocationErrorHandler("product-group", conf -> new ProductsServiceErrorHandler());
//		configurer.registerListenerInvocationErrorHandler("product-group", conf -> PropagatingErrorHandler.INSTANCE);
	}
}
