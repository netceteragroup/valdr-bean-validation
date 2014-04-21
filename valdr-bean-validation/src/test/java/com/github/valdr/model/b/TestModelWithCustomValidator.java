package com.github.valdr.model.b;

import com.github.valdr.model.validation.CustomValidation;
import org.hibernate.validator.constraints.Email;

public class TestModelWithCustomValidator {
  @CustomValidation
  private String customValidation;
  @Email
  private String email;
}
