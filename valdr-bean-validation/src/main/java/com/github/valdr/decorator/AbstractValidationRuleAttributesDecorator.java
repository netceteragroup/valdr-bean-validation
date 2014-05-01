package com.github.valdr.decorator;

import com.github.valdr.AttributeMap;
import com.github.valdr.ValidationRuleAttributes;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base implementation of a wrapper around {@link AttributeMap}. It ensures that all sub classes provide a
 * constructor that accepts such a map.
 */
public abstract class AbstractValidationRuleAttributesDecorator implements AttributeMap {

  @Getter(AccessLevel.PROTECTED)
  private final ValidationRuleAttributes decoratee;

  /**
   * Constructor that accepts the decoratee which is wrapped by this decorator.
   *
   * @param decoratee wrapped {@link AttributeMap}
   */
  public AbstractValidationRuleAttributesDecorator(ValidationRuleAttributes decoratee) {
    this.decoratee = decoratee;
  }
}
