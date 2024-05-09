package com.appsdeveloperblog.estore.usersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.appsdeveloperblog.estore.core.config.AxonConfig;

@SpringBootApplication
@Import({ AxonConfig.class })
public class UsersserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersserviceApplication.class, args);
	}

}
