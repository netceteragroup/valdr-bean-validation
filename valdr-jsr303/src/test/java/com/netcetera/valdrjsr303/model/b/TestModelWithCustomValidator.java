package com.netcetera.valdrjsr303.model.b;

import com.netcetera.valdrjsr303.model.validation.CustomValidation;

public class TestModelWithCustomValidator {
  @CustomValidation
  private String customValidation;
}
