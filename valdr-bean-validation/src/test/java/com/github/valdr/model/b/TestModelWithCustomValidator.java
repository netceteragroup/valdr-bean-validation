package com.github.valdr.model.b;

import com.github.valdr.model.validation.CustomValidation;

public class TestModelWithCustomValidator {
  @CustomValidation
  private String customValidation;
}
