package com.appsdeveloperblog.estore.productsservice.command.rest;

import java.util.Optional;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class EventsReplayController {
	private final EventProcessingConfiguration eventProcessingConfiguration;

	@PostMapping("/event-processor/{processorName}/reset")
	public ResponseEntity<String> replayEvents(@PathVariable("processorName") String processorName) {
		Optional<TrackingEventProcessor> trackingEventProcessor = eventProcessingConfiguration.eventProcessor(processorName, TrackingEventProcessor.class);
		if(trackingEventProcessor.isPresent()) {
			TrackingEventProcessor eventProcessor = trackingEventProcessor.get();
			eventProcessor.shutDown();
			eventProcessor.resetTokens();
			eventProcessor.start();
			return ResponseEntity.ok().body(String.format("The event processor with name [%s] has been reset", processorName));
		}
		return ResponseEntity.badRequest().body(String.format("The event processor with name [%s] is not a tracking event processor. Only tracking event processor is supported", processorName));
	}
}
