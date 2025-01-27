# KAFKA MESSAGE DISPLAY UI

This repository implements a simple Kafka message display UI with incorporated Producer for adding a configurable volume of messages to the queue.

The application currently runs locally as a Springboot application.  It will be dockerized.

---

## Components

The application is composed of 3 major components:

1. Message display UI
2. Middleware server
3. Message producer microservice


## System Requirements

The application is deployed as a Docker image built from Springboot.  An operating instance of Kafka with the needed topic created is required.

---

## Configuration

The following values are configurable via application.properties:

- spring.application.name=KafkaConsole
- server.port=8080
- kafka.bootstrap.servers=localhost:9092
- kafka.consumer.cycleTime=200
- kafka.consumer.topic=demo
- kafka.consumer.group.id=DemoConsumer
- kafka.consumer.autocommit=false
- kafka.producer.topic=demo
- kafka.producer.message.prefix=This is message #
- kafka.producer.message.count=10000

---

## Building a Docker Image

TBD

---

## Starting the application in Docker

TBD

---

## Using the application

### Initiating the display UI

Open a browser to the address http://<hostname>:8080.  The request will initialize the message consumer, which will wait until messages are available on the queue.  Once messages become available and are consumed, they will display in order received in each poll batch with one message per line.

### Initiating new messages

Open a browser to the address http://<hostname>:8080/produce.  The request will cause messages to be placed on the queue for the topic defined by kafka.producer.topic.  The number of messages produced will be equal to the value in kafka.producer.message.count.