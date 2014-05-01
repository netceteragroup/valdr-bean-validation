package com.github.valdr.decorator;

import com.github.valdr.ConstraintAttributes;

import java.util.Map;
import java.util.Set;

/**
 * Decorates the map of attributes of the {@link javax.validation.constraints.Pattern} constraint.
 */
public class PatternDecorator extends AbstractConstraintAttributesDecorator {

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
    for (Map.Entry<String, Object> entry : entrySet) {
      if (entry.getKey() == "regexp") {
        entry.setValue(javaToJavaScriptRegexpPattern(entry));
      }
    }
    return entrySet;
  }

  private String javaToJavaScriptRegexpPattern(Map.Entry<String, Object> entry) {
    return "/" + entry.getValue() + "/";
  }
}
