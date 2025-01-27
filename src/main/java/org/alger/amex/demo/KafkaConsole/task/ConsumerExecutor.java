package org.alger.amex.demo.KafkaConsole.task;


import org.alger.amex.demo.KafkaConsole.handler.MessageSender;
import org.alger.amex.demo.KafkaConsole.kafka.KafkaMessageConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerExecutor implements Runnable {
	@Autowired
	private KafkaMessageConsumer msgConsumer;
	
	MessageSender sender;
	
	@Override
    public void run() {
		msgConsumer.initialize(sender);
		msgConsumer.consume();
	}
	
	public void deactivate() {
		msgConsumer.deactivate();
	}

	public void setSender(MessageSender sender) {
		this.sender = sender;
	}
}
