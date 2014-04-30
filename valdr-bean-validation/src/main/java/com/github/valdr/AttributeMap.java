package com.github.valdr;

import java.util.Map;
import java.util.Set;

/**
 * Max-reduced map interface to allow for POJOs that delegate to an internal {@link Map} rather than extend one.
 */
public interface AttributeMap {
  /**
   * Returns all attributes.
   *
   * @return all attributes
   */
  Set<Map.Entry<String, Object>> entrySet();
}
