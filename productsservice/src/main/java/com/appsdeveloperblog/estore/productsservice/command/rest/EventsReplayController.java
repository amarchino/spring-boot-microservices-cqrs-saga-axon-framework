package com.appsdeveloperblog.estore.productsservice.command.rest;

import org.axonframework.config.EventProcessingConfiguration;
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
	public void replayEvents(@PathVariable("processorName") String processorName) {
		
	}
}
