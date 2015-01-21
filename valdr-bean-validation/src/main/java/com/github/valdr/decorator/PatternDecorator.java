package com.github.valdr.decorator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.valdr.ConstraintAttributes;

/**
 * Decorates the map of attributes of the {@link javax.validation.constraints.Pattern} constraint.
 */
public class PatternDecorator extends AbstractConstraintAttributesDecorator {

  /**
   * Constructor that accepts the decoratee which is wrapped by this decorator.
   *
   * @param decoratee wrapped {@link ConstraintAttributes}
   */
  public PatternDecorator(ConstraintAttributes decoratee) {
    super(decoratee);
  }

  /**
   * Modifies the original entry set such that the value for the 'regexp' attribute is a JavaScript regexp pattern.
   * The following transformations are applied:
   * <ul>
   * <li>adding '/' prefix and suffix turning a Java pattern like "abc" into "/abc/" for JavaScript</li>
   * </ul>
   *
   * @return the modified entry set
   */
  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    Set<Map.Entry<String, Object>> entrySet = getDecoratee().entrySet();
    Map<String, Object> result = new HashMap<>();
    for (Map.Entry<String, Object> entry : entrySet) {
      if ("regexp".equals(entry.getKey())) {
        result.put("value", javaToJavaScriptRegexpPattern(entry));
      } else {
          result.put(entry.getKey(), entry.getValue());
      }
    }
    return result.entrySet();
  }

  private String javaToJavaScriptRegexpPattern(Map.Entry<String, Object> entry) {
    return "/" + entry.getValue() + "/";
  }
}
