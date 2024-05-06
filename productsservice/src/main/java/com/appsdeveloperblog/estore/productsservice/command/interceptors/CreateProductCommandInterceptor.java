package com.appsdeveloperblog.estore.productsservice.command.interceptors;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.productsservice.command.CreateProductCommand;
import com.appsdeveloperblog.estore.productsservice.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.ProductLookupRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	private final ProductLookupRepository productLookupRepository;

	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
		return (index, command) -> {
			log.info("Interceptor command: {}", command.getPayloadType());
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
				ProductLookupEntity entity = productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());
				if(entity != null) {
					throw new IllegalArgumentException(String.format("Product with productId %s or title %s already exists", createProductCommand.getProductId(), createProductCommand.getTitle()));
				}
			}
			return command;
		};
	}

}
