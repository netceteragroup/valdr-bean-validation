package com.github.valdr.demo.model;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {
  @NotNull(message = "\\foo")
  private String firstName;
  @Size(min = 4, max = 31)
  private String lastName;
  @CreditCardNumber
  private String creditCardNumber;
  @URL
  private String url;
  @Pattern(regexp = "abc")
  private String addSlashPrefixSuffix;
  @Pattern(regexp = "\\abc\\.") // \a matches the bell character ;-)
  private String withBackslashes;
  @Pattern(regexp = "\\\\abc\\.")
  private String withMoreBackslashes;
}
