package com.pharosproduction.avro.evolution;

import com.pharosproduction.avro.CustomerV1;
import com.pharosproduction.avro.CustomerV2;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class SchemaEvolutionExamples {

  public static void main(String[] args) throws IOException {
    final CustomerV1 customerV1 = CustomerV1.newBuilder()
      .setAge(35)
      .setAutomatedEmail(false)
      .setFirstName("John")
      .setLastName("Doe")
      .setHeight(177f)
      .setWeight(70f)
      .build();
    System.out.println("CustomerV1: " + customerV1.toString());

    final SpecificDatumWriter<CustomerV1> datumWriter = new SpecificDatumWriter<>(CustomerV1.class);
    final DataFileWriter<CustomerV1> dataFileWriter = new DataFileWriter<>(datumWriter);
    dataFileWriter.create(customerV1.getSchema(), new File("customerv1.avro"));
    dataFileWriter.append(customerV1);
    dataFileWriter.close();
    System.out.println("Successfully wrote customerv1.avro");

    System.out.println("Reading customerv1.avro with V2 schema");

    final File file = new File("customerv1.avro");
    final SpecificDatumReader<CustomerV2> datumReaderV2 = new SpecificDatumReader<>(CustomerV2.class);
    final DataFileReader<CustomerV2> dataFileReaderV2 = new DataFileReader<>(file, datumReaderV2);

    while (dataFileReaderV2.hasNext()) {
      final CustomerV2 customerV2 = dataFileReaderV2.next();
      System.out.println("Customer V2: " + customerV2.toString());
    }
  }
}
