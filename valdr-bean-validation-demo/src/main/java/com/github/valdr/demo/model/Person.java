package com.github.valdr.demo.model;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Person class used for demo purposes.
 */
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
  @NotNull
  private String ignoredField;
}
