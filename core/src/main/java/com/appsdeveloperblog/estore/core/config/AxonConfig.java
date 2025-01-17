package com.appsdeveloperblog.estore.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thoughtworks.xstream.XStream;

@Configuration
public class AxonConfig {

	@Bean
	XStream xStream() {
		XStream xStream = new XStream();
		xStream.allowTypesByWildcard(new String[] { "com.appsdeveloperblog.estore.**" });
		return xStream;
	}
}
