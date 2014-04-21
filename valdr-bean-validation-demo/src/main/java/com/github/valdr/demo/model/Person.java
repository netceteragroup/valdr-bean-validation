package com.github.valdr.demo.model;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person {
  @NotNull
  private String firstName;
  @Size(min = 4, max = 31)
  private String lastName;
  @Email
  private String email;
}
