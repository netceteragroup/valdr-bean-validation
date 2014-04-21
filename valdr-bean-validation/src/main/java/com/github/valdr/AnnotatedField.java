package com.github.valdr;

import com.google.common.collect.Iterables;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Decorator around a field with Bean Validation (and possibly other) annotations. Allows to extract validation rules
 * based on those annotations.
 */
public class AnnotatedField {
  private final Field field;
  private final Iterable<Class<? extends Annotation>> relevantAnnotationClasses;

  /**
   * @param field                     decorated field
   * @param relevantAnnotationClasses only these annotation classes are considered when {@link
   *                                  AnnotatedField#extractValidationRules()} is invoked
   */
  AnnotatedField(Field field, Iterable<Class<? extends Annotation>> relevantAnnotationClasses) {
    this.field = field;
    this.relevantAnnotationClasses = relevantAnnotationClasses;
  }

  /**
   * Parses all annotations and builds validation rules for the relevant ones.
   *
   * @return validation rules (one per annotation)
   * @see AnnotatedField(Class, Iterable)
   */
  FieldValidationRules extractValidationRules() {
    Annotation[] annotations = field.getAnnotations();
    FieldValidationRules validationRules = new FieldValidationRules();

    for (Annotation annotation : annotations) {
      if (Iterables.contains(relevantAnnotationClasses, annotation.annotationType())) {
        ValidationRuleAttributes annotationAttributes = new ValidationRuleAttributes(annotation);
        SupportedValidator supportedValidator = SupportedValidator.valueOfAnnotationClassOrNull(annotation
          .annotationType());
        if (supportedValidator == null) {
          validationRules.put(annotation.annotationType().getName(), annotationAttributes);
        } else {
          validationRules.put(supportedValidator.name(), annotationAttributes);
        }
      }
    }

    return validationRules;
  }
}
