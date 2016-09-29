package com.github.valdr;

import com.github.valdr.thirdparty.spring.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * All attributes of a constraint (i.e. Bean Validation annotation attributes).
 */
public class ConstraintAttributes implements MinimalObjectMap {

  private final Map<String, Object> map = new HashMap<>();
  private final boolean outputValidationGroups;
  private static final String GROUPS = "groups";

  /**
   * Constructor.
   *
   * @param annotation annotation which is queried for attributes
   */
  public ConstraintAttributes(Annotation annotation, boolean outputValidationGroups) {
    this.outputValidationGroups = outputValidationGroups;
    Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
    removeUnusedAttributes(annotationAttributes);
    if (outputValidationGroups) {
      validationGroupClassNamesToSimple(annotationAttributes);
    }

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

  private void removeUnusedAttributes(Map<String, Object> annotationAttributes) {
    Iterator<String> it = annotationAttributes.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      if ("payload".equals(key) || (GROUPS.equals(key) && !outputValidationGroups)) {
        it.remove();
      }
    }
  }

  private void validationGroupClassNamesToSimple(Map<String, Object> annotationAttributes) {
    Class[] groupClasses = (Class[])annotationAttributes.get(GROUPS);
    if (groupClasses.length > 0) {
      List<String> simpleNames = new ArrayList<>();
      for(Class groupClass: groupClasses) {
        for(Class c: getThisAndAncestors(groupClass)) {
          simpleNames.add(c.getSimpleName());
        }
      }
      annotationAttributes.put(GROUPS, simpleNames);
    } else {
      annotationAttributes.put(GROUPS, new String[]{"Default"});
    }
  }

  private List<Class> getThisAndAncestors(Class aClass) {
    List<Class> ret = new ArrayList<>();
    if (!aClass.equals(Object.class)) {
      ret.add(aClass);
    }
    Class[] parents = aClass.getInterfaces();
    for(Class parent: parents) {
      ret.addAll(getThisAndAncestors(parent));
    }
    return ret;
  }
}
