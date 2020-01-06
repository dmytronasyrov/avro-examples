package com.pharosproduction.avro.reflection;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.File;
import java.io.IOException;

public class ReflectionExample {

  public static void main(String[] args) {
    final Schema schema = ReflectData.get().getSchema(ReflectedCustomer.class);
    System.out.println("Schema: " + schema.toString());

    try {
      System.out.println("Writing customer-reflected.avro");
      final File file = new File("customer-reflected.avro");
      DatumWriter<ReflectedCustomer> writer = new ReflectDatumWriter<>(ReflectedCustomer.class);
      DataFileWriter<ReflectedCustomer> out = new DataFileWriter<>(writer)
        .setCodec(CodecFactory.deflateCodec(9))
        .create(schema, file);
      out.append(new ReflectedCustomer("John", "Doe", "Rocket"));
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      System.out.println("Reading customer-reflected.avro");
      final File file = new File("customer-reflected.avro");
      final DatumReader<ReflectedCustomer> reader = new ReflectDatumReader<>(ReflectedCustomer.class);
      final DataFileReader<ReflectedCustomer> in = new DataFileReader<>(file, reader);

      for (ReflectedCustomer reflectedCustomer : in) {
        System.out.println(reflectedCustomer.fullName());
      }

      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
