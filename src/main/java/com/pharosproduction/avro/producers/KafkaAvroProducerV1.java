package com.pharosproduction.avro.producers;

import com.pharosproduction.avro.specific.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaAvroProducerV1 {

  public static void main(String[] args) {
    final Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
    properties.setProperty("group.id", "customer-consumer-group-v1");
    properties.setProperty("auto.commit.enable", "false");
    properties.setProperty("auto.offset.reset", "earliest");
    properties.setProperty("key.serializer", StringSerializer.class.getName());
    properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
    properties.setProperty("key.deserializer", StringDeserializer.class.getName());
    properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
    properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

    final KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<>(properties);
    final String topic = "customer-avro";

    final Customer.Builder customerBuilder = Customer.newBuilder()
      .setFirstName("John")
      .setLastName("Doe")
      .setAge(25)
      .setWeight(80.5f)
      .setHeight(177f);
    final Customer customer = customerBuilder.build();
    final ProducerRecord<String, Customer> producerRecord = new ProducerRecord<>(topic, customer);

    kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
      if (e != null)
        e.printStackTrace();
      else
        System.out.println("Success: " + recordMetadata.toString());
    });
    kafkaProducer.flush();
    kafkaProducer.close();
  }
}
