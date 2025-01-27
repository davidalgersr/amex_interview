package org.alger.amex.demo.KafkaConsole.kafka;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.alger.amex.demo.KafkaConsole.handler.MessageSender;
import org.alger.amex.demo.KafkaConsole.model.KafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Component
public class KafkaMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    
	@Value("${kafka.bootstrap.servers}")
	private String kafkaBoostrapServers;
    		
	@Value("${demo.consumer.cycleTime:100}")
	private long delayInMillis;
	
	@Value("${kafka.consumer.topic}")
	private String topic;
	
	@Value("${kafka.consumer.group.id}")
	private String groupId;
	
	@Value("${kafka.consumer.autocommit:false}")
	private String enableAutoCommit;

	private KafkaConsumer<String, String> consumer;
	
	private boolean isActivated = false;
	
	private MessageSender sender;

	public void initialize(MessageSender sender) {
		this.sender = sender;
		
		Properties props = new Properties();
		props.setProperty(BOOTSTRAP_SERVERS_CONFIG, kafkaBoostrapServers);
		props.setProperty("group.id", groupId);
		props.setProperty("enable.auto.commit", enableAutoCommit);
		props.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		
		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topic));
	}
	
	public void consume() {	
        ObjectMapper objectMapper = new ObjectMapper();
        
        long totalConsumedCount = 0;
		
		KafkaMessage msg = new KafkaMessage();
        
		isActivated = true;

		if(consumer != null) {
			while(isActivated) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(delayInMillis));
				
				if(logger.isDebugEnabled()) logger.debug("Records in poll: %d", records.count());

				try {
					for (ConsumerRecord record : records.records(topic)) {
						msg.setValue(record.value().toString());
						var message = new TextMessage(objectMapper.writeValueAsString(msg).getBytes());
						sender.sendMessage(message);
					}
					consumer.commitSync();
				} catch (IOException ex) {
					System.out.println("Exception while sending messages: "+ex.getMessage());
				}
			}
		}
	}
	
	public void deactivate() {
		isActivated = false;
	}
}
