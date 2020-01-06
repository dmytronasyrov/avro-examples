package com.pharosproduction.avro.specific;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class SpecificRecordExample {

  public static void main(String[] args) {
    final Customer.Builder customerBuilder = Customer.newBuilder()
      .setFirstName("John")
      .setLastName("Doe")
      .setAge(25)
      .setWeight(80.5f)
      .setHeight(177f);
    final Customer customer = customerBuilder.build();
    System.out.println(customer);

    final SpecificDatumWriter<Customer> datumWriter = new SpecificDatumWriter<>(Customer.class);
    try (DataFileWriter<Customer> dataFileWriter = new DataFileWriter<>(datumWriter)) {
      dataFileWriter.create(customer.getSchema(), new File("customer-specific.avro"));
      dataFileWriter.append(customer);
      System.out.println("Successfully written customer-specific.avro");
    } catch (IOException e) {
      e.printStackTrace();
    }

    final File file = new File("customer-specific.avro");
    final DatumReader<Customer> datumReader = new SpecificDatumReader<>(Customer.class);
    try {
      System.out.println("Reading customer-specific.avro");
      final DataFileReader<Customer> dataFileReader = new DataFileReader<>(file, datumReader);

      while (dataFileReader.hasNext()) {
        final Customer readCustomer = dataFileReader.next();
        System.out.println(readCustomer.toString());
        System.out.println("First name: " + readCustomer);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
