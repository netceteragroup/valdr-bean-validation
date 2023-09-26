package com.github.valdr.thirdparty.spring;
/*
 * This class was copied and modified from the Spring framework. Thanks!
 */


/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.base.Joiner;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link java.util.LinkedHashMap} subclass representing annotation attribute key/value pairs
 * as read by Spring's reflection- or ASM-based {@code AnnotationMetadata} implementations.
 * Provides 'pseudo-reification' to avoid noisy Map generics in the calling code as well
 * as convenience methods for looking up annotation attributes in a type-safe fashion.
 *
 * @author Chris Beams
 * @since 3.1.1
 */
@SuppressWarnings("serial")
public class AnnotationAttributes extends LinkedHashMap<String, Object> {

  /**
   * Create a new, empty {@link AnnotationAttributes} instance.
   */
  public AnnotationAttributes() {
  }

  /**
   * Create a new, empty {@link AnnotationAttributes} instance with the given initial
   * capacity to optimize performance.
   *
   * @param initialCapacity initial size of the underlying map
   */
  public AnnotationAttributes(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Create a new {@link AnnotationAttributes} instance, wrapping the provided map
   * and all its key/value pairs.
   *
   * @param map original source of annotation attribute key/value pairs to wrap
   * @see #fromMap(java.util.Map)
   */
  public AnnotationAttributes(Map<String, Object> map) {
    super(map);
  }

  /**
   * Return an {@link AnnotationAttributes} instance based on the given map; if the map
   * is already an {@code AnnotationAttributes} instance, it is casted and returned
   * immediately without creating any new instance; otherwise create a new instance by
   * wrapping the map with the {@link #AnnotationAttributes(java.util.Map)} constructor.
   *
   * @param map original source of annotation attribute key/value pairs
   * @return annotation attributes
   */
  public static AnnotationAttributes fromMap(Map<String, Object> map) {
    if (map == null) {
      return null;
    }

    if (map instanceof AnnotationAttributes) {
      return (AnnotationAttributes) map;
    }

    return new AnnotationAttributes(map);
  }

  /**
   * Gets value of type {@link String} for the given attribute name.
   *
   * @param attributeName attribute name
   * @return value of type {@link String}
   */
  public String getString(String attributeName) {
    return doGet(attributeName, String.class);
  }

  /**
   * Gets value of type {@link String} array for the given attribute name.
   *
   * @param attributeName attribute name
   * @return value of type {@link String} array
   */
  public String[] getStringArray(String attributeName) {
    return doGet(attributeName, String[].class);
  }

  /**
   * Gets value of type {@link Boolean} for the given attribute name.
   *
   * @param attributeName attribute name
   * @return value of type {@link Boolean}
   */
  public boolean getBoolean(String attributeName) {
    return doGet(attributeName, Boolean.class);
  }

  /**
   * Gets value of type {@link Number} for the given attribute name.
   *
   * @param attributeName attribute name
   * @param <N>           type of {@link Number}
   * @return value of type {@link Number}
   */
  @SuppressWarnings("unchecked")
  public <N extends Number> N getNumber(String attributeName) {
    return (N) doGet(attributeName, Integer.class);
  }

  /**
   * Gets value of type {@link Enum} for the given attribute name.
   *
   * @param attributeName attribute name
   * @param <E>           extends Enum
   * @return value of type {@link Enum}
   */
  @SuppressWarnings("unchecked")
  public <E extends Enum<?>> E getEnum(String attributeName) {
    return (E) doGet(attributeName, Enum.class);
  }

  /**
   * Gets value of type {@link Class} for the given attribute name.
   *
   * @param attributeName attribute name
   * @param <T>           class
   * @return value of type {@link Class}
   */
  @SuppressWarnings("unchecked")
  public <T> Class<? extends T> getClass(String attributeName) {
    return doGet(attributeName, Class.class);
  }

  /**
   * Gets value of type {@link Class} array for the given attribute name.
   *
   * @param attributeName attribute name
   * @return value of type {@link Class} array
   */
  public Class<?>[] getClassArray(String attributeName) {
    return doGet(attributeName, Class[].class);
  }

  /**
   * Gets value of type {@link AnnotationAttributes} for the given attribute name.
   *
   * @param attributeName attribute name
   * @return value of type {@link AnnotationAttributes}
   */
  public AnnotationAttributes getAnnotation(String attributeName) {
    return doGet(attributeName, AnnotationAttributes.class);
  }

  /**
   * Gets value of type {@link AnnotationAttributes} array for the given attribute name.
   *
   * @param attributeName attribute name
   * @return value of type {@link AnnotationAttributes} array
   */
  public AnnotationAttributes[] getAnnotationArray(String attributeName) {
    return doGet(attributeName, AnnotationAttributes[].class);
  }

  @SuppressWarnings("unchecked")
  private <T> T doGet(String attributeName, Class<T> expectedType) {
    Object value = this.get(attributeName);
    return (T) value;
  }

  public String toString() {
    Iterator<Map.Entry<String, Object>> entries = entrySet().iterator();
    StringBuilder sb = new StringBuilder("{");
    while (entries.hasNext()) {
      Map.Entry<String, Object> entry = entries.next();
      sb.append(entry.getKey());
      sb.append('=');
      sb.append(valueToString(entry.getValue()));
      sb.append(entries.hasNext() ? ", " : "");
    }
    sb.append("}");
    return sb.toString();
  }

  private String valueToString(Object value) {
    if (value == this) {
      return "(this Map)";
    }
    if (value instanceof Object[]) {
      return "[" + Joiner.on(',').join((Object[]) value) + "]";
    }
    return String.valueOf(value);
  }
}
