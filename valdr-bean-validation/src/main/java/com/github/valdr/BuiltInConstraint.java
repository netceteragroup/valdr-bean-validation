package com.github.valdr;

import com.github.valdr.decorator.AbstractConstraintAttributesDecorator;
import com.github.valdr.decorator.NullDecorator;
import com.github.valdr.decorator.PatternDecorator;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * All constraints currently supported out-of-the-box by valdr Bean Validation. Each value represents a constraint
 * that valdr provides an implementation for (JavaScript). The {@code toString()} method is guaranteed to return the
 * validator name as expected by valdr.
 */
public enum BuiltInConstraint {

  // CHECKSTYLE:OFF
  REQUIRED("required", NotNull.class),
  MIN("min", Min.class),
  MAX("max", Max.class),
  SIZE("size", Size.class),
  DIGITS("digits", Digits.class),
  PATTERN("pattern", Pattern.class, PatternDecorator.class),
  FUTURE("future", Future.class),
  PAST("past", Past.class),
  EMAIL("email", Email.class),
  URL("hibernateUrl", org.hibernate.validator.constraints.URL.class);
  // CHECKSTYLE:ON

  @Getter
  private final Class<? extends Annotation> beanValidationAnnotation;
  private final Class<? extends AbstractConstraintAttributesDecorator> decorator;
  private final String camelCaseName;

  BuiltInConstraint(String camelCaseName, Class<? extends Annotation> beanValidationAnnotation) {
    this(camelCaseName, beanValidationAnnotation, NullDecorator.class);
  }

  BuiltInConstraint(String camelCaseName, Class<? extends Annotation> beanValidationAnnotation,
    Class<? extends AbstractConstraintAttributesDecorator> decorator) {
    this.camelCaseName = camelCaseName;
    this.decorator = decorator;
    this.beanValidationAnnotation = beanValidationAnnotation;
  }

  @Override
  public String toString() {
    return camelCaseName;
  }

  /**
   * Wraps a decorator of this constraint around the constraint attributes and returns it.
   *
   * @param attributes the attributes to decorate
   * @return decorator
   */
  @SneakyThrows(ReflectiveOperationException.class)
  public AbstractConstraintAttributesDecorator createDecoratorFor(ConstraintAttributes attributes) {
    return decorator.getConstructor(ConstraintAttributes.class).newInstance(attributes);
  }

  /**
   * Iterates over all the enum values and collects their annotation class member.
   *
   * @return all collected annotation classes
   */
  public static Iterable<Class<? extends Annotation>> getAllBeanValidationAnnotations() {
    return Arrays.stream(values()).map(input -> {
      if (input == null) {
        throw new NullPointerException("Passed validator must not be null.");
      }
      return input.getBeanValidationAnnotation();
    }).collect(Collectors.toList());
  }

  /**
   * Finds enum value whose annotation class member matches the method argument.
   *
   * @param beanValidationAnnotation annotation class
   * @return enum value matching the passed annotation or null
   */
  public static BuiltInConstraint valueOfAnnotationClassOrNull(Class<? extends Annotation> beanValidationAnnotation) {
    for (BuiltInConstraint supportedValidator : values()) {
      if (supportedValidator.getBeanValidationAnnotation().equals(beanValidationAnnotation)) {
        return supportedValidator;
      }
    }
    return null;
  }
}
