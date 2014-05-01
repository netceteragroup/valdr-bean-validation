package com.github.valdr;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

/**
 * Wrapper around a class with Bean Validation (and possibly other) annotations. Allows to extract validation rules
 * based on those annotations.
 */
public class AnnotatedClass {
  private final Class clazz;
  private final Iterable<Class<? extends Annotation>> relevantAnnotationClasses;

  /**
   * @param clazz                     wrapped class
   * @param relevantAnnotationClasses only these annotation classes are considered when {@link
   *                                  AnnotatedClass#extractValidationRules()} is invoked
   */
  AnnotatedClass(Class clazz, Iterable<Class<? extends Annotation>> relevantAnnotationClasses) {
    this.clazz = clazz;
    this.relevantAnnotationClasses = relevantAnnotationClasses;
  }

  /**
   * Parses all fields and builds validation rules for those with relevant annotations.
   *
   * @return validation rules for all fields that have at least one rule
   * @see AnnotatedClass(Class, Iterable)
   */
  ClassConstraints extractValidationRules() {
    final ClassConstraints fieldNameToValidationRulesMap = new ClassConstraints();
    Set<Field> allFields = ReflectionUtils.getAllFields(clazz, buildAnnotationsPredicate());
    for (Field field : allFields) {
      FieldConstraints fieldValidationRules = new AnnotatedField(field,
        relevantAnnotationClasses).extractValidationRules();
      if (fieldValidationRules.size() > 0) {
        fieldNameToValidationRulesMap.put(field.getName(), fieldValidationRules);
      }
    }
    return fieldNameToValidationRulesMap;
  }

  private Predicate<? super Field> buildAnnotationsPredicate() {
    Collection<Predicate<? super Field>> predicates = Lists.newArrayList();
    for (Class<? extends Annotation> annotationClass : relevantAnnotationClasses) {
      predicates.add(ReflectionUtils.withAnnotation(annotationClass));
    }
    return Predicates.or(predicates);
  }
}
