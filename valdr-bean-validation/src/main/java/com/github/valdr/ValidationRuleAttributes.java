package com.github.valdr;

import com.github.valdr.thirdparty.spring.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ValidationRuleAttributes extends HashMap<String, Object> {

  ValidationRuleAttributes(Annotation annotation) {
    Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
    removeUnusedAttributes(annotationAttributes);
    putAll(annotationAttributes);
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
