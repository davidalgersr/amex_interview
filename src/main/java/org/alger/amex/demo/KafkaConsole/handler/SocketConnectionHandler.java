package org.alger.amex.demo.KafkaConsole.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alger.amex.demo.KafkaConsole.task.ConsumerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Component
public class SocketConnectionHandler extends TextWebSocketHandler implements MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(SocketConnectionHandler.class);

	private List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());

	@Autowired
	private ConsumerExecutor executor;

	private TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
	
	public SocketConnectionHandler(ConsumerExecutor executor) {
		this.executor = executor;
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		
		if(logger.isInfoEnabled()) logger.info(String.format("Session ID %s connected.", session.getId()));

		webSocketSessions.add(session);
		
		executor.setSender(this);
		taskExecutor.execute(executor);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		
		if(logger.isInfoEnabled()) logger.info(String.format("Session ID %s disconnected.", session.getId()));

		webSocketSessions.remove(session);
		
		if(webSocketSessions.size() == 0) {
			executor.deactivate();
		}
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		
		if(logger.isDebugEnabled()) logger.debug(String.format("Sending message to %d session: %s", webSocketSessions.size(), message.getPayload().toString()));
		
		for (WebSocketSession webSocketSession : webSocketSessions) {
			webSocketSession.sendMessage(message);
		}
	}

	public void sendMessage(TextMessage message) throws IOException {		
		if(logger.isDebugEnabled()) logger.debug(String.format("Sending text message to %d session: %s", webSocketSessions.size(), message.getPayload().toString()));
		
		for (WebSocketSession webSocketSession : webSocketSessions) {
			webSocketSession.sendMessage(message);
		}
	}
}