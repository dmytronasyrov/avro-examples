package com.pharosproduction.avro.reflection;

import org.apache.avro.reflect.Nullable;

public class ReflectedCustomer {

  private String firstName;
  private String lastName;
  @Nullable private String username;

  public ReflectedCustomer() {
  }

  ReflectedCustomer(String firstName, String lastName, String username) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
  }

  public String fullName() {
    return firstName + " " + lastName + ", " + username;
  }

  String getFirstName() {
    return firstName;
  }

  void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  String getUsername() {
    return username;
  }

  void setUsername(String username) {
    this.username = username;
  }
}
