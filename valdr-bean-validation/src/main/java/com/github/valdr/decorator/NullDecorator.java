package com.github.valdr.decorator;

import com.github.valdr.ValidationRuleAttributes;

import java.util.Map;
import java.util.Set;

/**
 * Do-nothing implementation.
 */
public class NullDecorator extends AbstractValidationRuleAttributesDecorator {
  // CHECKSTYLE:OFF
  public NullDecorator(ValidationRuleAttributes decoratee) {
    super(decoratee);
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return getDecoratee().entrySet();
  }
}