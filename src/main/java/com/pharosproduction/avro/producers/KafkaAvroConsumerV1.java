package com.pharosproduction.avro.producers;

import com.pharosproduction.avro.specific.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaAvroConsumerV1 {

  public static void main(String[] args) {
    final Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
    properties.setProperty("group.id", "customer-consumer-group-v1");
    properties.setProperty("auto.commit.enable", "false");
    properties.setProperty("auto.offset.reset", "earliest");
    properties.setProperty("acks", "1");
    properties.setProperty("retries", "10");
    properties.setProperty("key.serializer", StringSerializer.class.getName());
    properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
    properties.setProperty("key.deserializer", StringDeserializer.class.getName());
    properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
    properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
    properties.setProperty("specific.avro.reader", "true");

    final KafkaConsumer<String, Customer> consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singleton("customer-avro"));

    System.out.println("Waiting for data...");

    while (true) {
      final ConsumerRecords<String, Customer> records = consumer.poll(Duration.ofMillis(500));

      for (ConsumerRecord<String, Customer> record : records) {
        System.out.println("Customer: " + record.value());
      }

      consumer.commitSync();
    }
  }
}
