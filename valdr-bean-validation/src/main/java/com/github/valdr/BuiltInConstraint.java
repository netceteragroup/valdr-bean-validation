package com.github.valdr;

import com.github.valdr.decorator.AbstractConstraintAttributesDecorator;
import com.github.valdr.decorator.NullDecorator;
import com.github.valdr.decorator.PatternDecorator;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * <p> All constraints currently supported out-of-the-box by valdr Bean Validation.
 * <p> Each value represents a constraint that valdr provides an implementation for (JavaScript).
 * <p> The {@code toString()} method is guaranteed to return the validator name as expected by valdr.
 */
public enum BuiltInConstraint {

  // CHECKSTYLE:OFF
  /**
   * Required validation constraint.
   */
  REQUIRED("required", NotNull.class),
  /**
   * Min validation constraint.
   */
  MIN("min", Min.class),
  /**
   * Max validation constraint.
   */
  MAX("max", Max.class),
  /**
   * Size validation constraint.
   */
  SIZE("size", Size.class),
  /**
   * Number validation constraint.
   */
  DIGITS("digits", Digits.class),
  /**
   * Pattern validation constraint.
   */
  PATTERN("pattern", Pattern.class, PatternDecorator.class),
  /**
   * Date/Time in the future validation constraint.
   */
  FUTURE("future", Future.class),
  /**
   * Date/Time in the past validation constraint.
   */
  PAST("past", Past.class),
  /**
   * Email validation constraint.
   */
  EMAIL("email", Email.class),
  /**
   * URL validation constraint.
   */
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
