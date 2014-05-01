package com.github.valdr.decorator;

import com.github.valdr.AttributeMap;
import com.github.valdr.ConstraintAttributes;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base implementation of a wrapper around {@link ConstraintAttributes}. It ensures that all sub classes provide a
 * constructor that accepts such a map.
 */
public abstract class AbstractConstraintAttributesDecorator implements AttributeMap {

  @Getter(AccessLevel.PROTECTED)
  private final ConstraintAttributes decoratee;

  /**
   * Constructor that accepts the decoratee which is wrapped by this decorator.
   *
   * @param decoratee wrapped {@link ConstraintAttributes}
   */
  public AbstractConstraintAttributesDecorator(ConstraintAttributes decoratee) {
    this.decoratee = decoratee;
  }
}
