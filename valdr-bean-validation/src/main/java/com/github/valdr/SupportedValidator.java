package com.github.valdr;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum SupportedValidator {

  REQUIRED("required", NotNull.class), MIN("min", Min.class), MAX("max", Max.class), SIZE("size", Size.class),
  DIGITS("digits", Digits.class), PATTERN("pattern", Pattern.class), FUTURE("future", Future.class), PAST("past",
    Past.class), EMAIL("hibernateEmail", Email.class), URL("hibernateUrl", org.hibernate.validator.constraints.URL
    .class);

  @Getter
  private final Class<? extends Annotation> beanValidationAnnotation;
  private final String camelCaseName;

  private SupportedValidator(String camelCaseName, Class<? extends Annotation> beanValidationAnnotation) {
    this.camelCaseName = camelCaseName;
    this.beanValidationAnnotation = beanValidationAnnotation;
  }

  @Override
  public String toString() {
    return camelCaseName;
  }

  public static Iterable<Class<? extends Annotation>> getAllBeanValidationAnnotations() {
    return Iterables.transform(Arrays.asList(values()), new Function<SupportedValidator,
      Class<? extends Annotation>>() {

      @Override
      public Class<? extends Annotation> apply(SupportedValidator input) {
        if (input == null) {
          throw new NullPointerException("Passed validator must not be null.");
        }
        return input.getBeanValidationAnnotation();
      }
    });
  }

  public static SupportedValidator valueOfAnnotationClassOrNull(Class<? extends Annotation> beanValidationAnnotation) {
    for (SupportedValidator supportedValidator : values()) {
      if (supportedValidator.getBeanValidationAnnotation().equals(beanValidationAnnotation)) {
        return supportedValidator;
      }
    }
    return null;
  }
}
