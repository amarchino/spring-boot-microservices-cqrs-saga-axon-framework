package com.appsdeveloperblog.estore.ordersservice.errorhandling;

import java.time.Instant;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.appsdeveloperblog.estore.core.errorhandling.ErrorMessage;

@ControllerAdvice
public class OrdersControllerErrorHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalArgumentException exception, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(Instant.now(), exception.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleException(Exception exception, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(Instant.now(), exception.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CommandExecutionException.class)
	public ResponseEntity<ErrorMessage> handleCommandExecutionException(CommandExecutionException exception, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(Instant.now(), exception.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
