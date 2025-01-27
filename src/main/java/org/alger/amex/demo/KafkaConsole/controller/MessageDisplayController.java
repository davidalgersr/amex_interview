package org.alger.amex.demo.KafkaConsole.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageDisplayController {
    private static final Logger logger = LoggerFactory.getLogger(MessageDisplayController.class);

	@GetMapping("/")
	public String console() {		
		if(logger.isDebugEnabled()) logger.debug("Request to display messages received.");
		
		return "index.html";
	}
}