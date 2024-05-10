package com.appsdeveloperblog.estore.core.errorhandling;

import java.time.Instant;

import lombok.Data;

@Data
public class ErrorMessage {
	private final Instant timestamp;
	private final String message;
}
