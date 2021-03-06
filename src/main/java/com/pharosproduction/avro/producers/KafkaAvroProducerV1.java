package com.pharosproduction.avro.producers;

import com.pharosproduction.avro.CustomerV1;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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

    final KafkaProducer<String, CustomerV1> kafkaProducer = new KafkaProducer<>(properties);
    final String topic = "customer-avro";

    final CustomerV1.Builder customerBuilder = CustomerV1.newBuilder()
      .setFirstName("John")
      .setLastName("Doe")
      .setAge(25)
      .setWeight(80.5f)
      .setHeight(177f)
      .setAutomatedEmail(false);
    final CustomerV1 customer = customerBuilder.build();
    final ProducerRecord<String, CustomerV1> producerRecord = new ProducerRecord<>(topic, customer);

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
