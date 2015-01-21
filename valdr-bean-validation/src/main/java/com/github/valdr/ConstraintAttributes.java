package com.github.valdr;

import com.github.valdr.thirdparty.spring.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * All attributes of a constraint (i.e. Bean Validation annotation attributes).
 */
public class ConstraintAttributes implements MinimalObjectMap {

  private final Map<String, Object> map = new HashMap<>();

  /**
   * Constructor.
   *
   * @param annotation annotation which is queried for attributes
   */
  public ConstraintAttributes(Annotation annotation) {
    Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
    removeUnusedAttributes(annotationAttributes);
    map.putAll(annotationAttributes);
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return map.entrySet();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Object put(String key, Object value) {
    return map.put(key, value);
  }
  
  public Object remove(String key) {
      return map.remove(key);
  }
  
  public Object get(String key) {
      return map.get(key);
  }

  private void removeUnusedAttributes(Map<String, Object> annotationAttributes) {
    Iterator<String> it = annotationAttributes.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      if ("groups".equals(key) || "payload".equals(key)) {
        it.remove();
      }
    }
  }
}
