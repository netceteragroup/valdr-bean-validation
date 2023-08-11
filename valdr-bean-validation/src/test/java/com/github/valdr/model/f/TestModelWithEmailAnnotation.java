package com.github.valdr.model.f;

import jakarta.validation.constraints.Email;

public class TestModelWithEmailAnnotation {
  @Email
  private String email;
}
