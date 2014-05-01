package com.github.valdr.decorator;

import com.github.valdr.ConstraintAttributes;

import java.util.Map;
import java.util.Set;

/**
 * Do-nothing implementation.
 */
public class NullDecorator extends AbstractConstraintAttributesDecorator {
  // CHECKSTYLE:OFF
  public NullDecorator(ConstraintAttributes decoratee) {
    super(decoratee);
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return getDecoratee().entrySet();
  }
}