package org.alger.amex.demo.KafkaConsole.handler;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;

public interface MessageSender {
	public void sendMessage(TextMessage message) throws IOException;
}
