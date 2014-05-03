package com.github.valdr.decorator;

import com.github.valdr.ConstraintAttributes;
import com.github.valdr.MinimalObjectMap;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base implementation of a wrapper around {@link ConstraintAttributes}. It ensures that all sub classes provide a
 * constructor that accepts such a map.
 */
public abstract class AbstractConstraintAttributesDecorator implements MinimalObjectMap {

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

  @Override
  public Object put(String key, Object value) {
    return decoratee.put(key, value);
  }

  @Override
  public int size() {
    return decoratee.size();
  }
}
