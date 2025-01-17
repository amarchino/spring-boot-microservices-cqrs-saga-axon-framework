package com.appsdeveloperblog.estore.paymentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.appsdeveloperblog.estore.core.config.AxonConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ AxonConfig.class })
public class PaymentsserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentsserviceApplication.class, args);
	}

}
