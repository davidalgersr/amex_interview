package org.alger.amex.demo.KafkaConsole.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public class KafkaTextMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTextMessageProducer.class);
    
	@Value("${kafka.bootstrap.servers}")
	private String kafkaBoostrapServers;
	
	@Value("${kafka.producer.topic}")
	private String kafkaTopic;
	
	@Value("${kafka.producer.message.prefix}")
	private String messagePrefix;
	
	@Value("${kafka.producer.message.count}")
	private long messageMax;

	public void produceMessages() {
		final Properties props = new Properties() {
			{
				put(BOOTSTRAP_SERVERS_CONFIG, kafkaBoostrapServers);
				put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
				put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
			}
		};
		
		if(logger.isDebugEnabled()) logger.debug(String.format("Bootstrap servers: %s", kafkaBoostrapServers));

		try (final Producer<String, String> producer = new KafkaProducer<>(props)) {
			for (int i = 1; i <= messageMax; i++) {
				String message = String.format("%s%05d", messagePrefix, i);
				producer.send(new ProducerRecord<>(kafkaTopic, message), (event, ex) -> {
					if (ex != null)
						if(logger.isErrorEnabled()) logger.error(String.format("Message send failed."));
					else
						if(logger.isInfoEnabled()) logger.info(String.format("Produced message to topic %s: %s%n", kafkaTopic, message));
				});
			}
			if(logger.isInfoEnabled()) logger.info(String.format("%s events were produced to topic %s%n", messageMax, kafkaTopic));
		}
	}
}