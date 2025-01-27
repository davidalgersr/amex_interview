package org.alger.amex.demo.KafkaConsole.controller;

import org.alger.amex.demo.KafkaConsole.kafka.KafkaTextMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MessageProducerController {
    private static final Logger logger = LoggerFactory.getLogger(MessageProducerController.class);
    		
	@Autowired
	private KafkaTextMessageProducer textMsgProducer;
	
	@GetMapping("/produce")
	@ResponseStatus(value = HttpStatus.OK)
	public void produceTextMessages() {
		if(logger.isDebugEnabled()) logger.debug("Request to produce messages received.");
		
		textMsgProducer.produceMessages();
	}
}
