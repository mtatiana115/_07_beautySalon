package com.riwi.beautySalon.domain.entities;

import jakarta.persistence.Entity;

@Entity( name = "employee")
public class Employee {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String role;

}
