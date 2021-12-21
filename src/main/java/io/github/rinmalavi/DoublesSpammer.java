package io.github.rinmalavi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DoublesSpammer {

    private static final Logger log = LoggerFactory.getLogger(DoublesSpammer.class);

    static final String serverUrl = "localhost:43210";
    static final String topicName = "prices";

    public static void main(String[] args) {
        Random random = new Random();
        for (long i = 0; i < 300; i++) {
            sendMessage("" + i, topicName, random.nextDouble());
        }
    }


    private static void sendMessage(String key, String outgoingTopic, Double trd) {
        Producer<String, Double> producer = createProducer();
        log.info("Sending message to queue {}", outgoingTopic);
        ProducerRecord<String, Double> producerRecord = new ProducerRecord<>(outgoingTopic, key, trd);
        Future<RecordMetadata> futureMetadata = producer.send(producerRecord);
        RecordMetadata recordMetadata = null;
        try {
            recordMetadata = futureMetadata.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("offset:" + recordMetadata.offset());
    }

    public static Producer<String, Double> createProducer() {
        Producer<String, Double> producer;
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", serverUrl);
        producerProps.put("acks", "all");
        producerProps.put("key.serializer", StringSerializer.class.getCanonicalName());
        producerProps.put("value.serializer", DoubleSerializer.class.getCanonicalName());
        producerProps.put("max.request.size", "1000000000");
        producerProps.put("send.buffer.bytes", "1000000000");
        producerProps.put("buffer.memory", "1000000000");
        producer = new KafkaProducer<>(producerProps);

        return producer;
    }
}
