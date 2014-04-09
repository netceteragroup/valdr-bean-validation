package com.github.valdr;

import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Decorator around a field with Bean Validation (and possibly other) annotations. Allows to extract validation rules
 * based on those annotations.
 */
public class AnnotatedField {
  private final Logger logger = LoggerFactory.getLogger(AnnotatedField.class);
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
          logger.warn("No supported validator found for '{}', ignoring.", annotation.annotationType());
        } else {
          validationRules.put(supportedValidator, annotationAttributes);
        }
      }
    }

    return validationRules;
  }
}
