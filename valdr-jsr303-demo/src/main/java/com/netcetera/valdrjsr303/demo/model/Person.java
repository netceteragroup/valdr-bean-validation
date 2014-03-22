package com.netcetera.valdrjsr303.demo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person {
  @NotNull
  private String firstName;
  @Size(min = 4, max = 31)
  private String lastName;
}
