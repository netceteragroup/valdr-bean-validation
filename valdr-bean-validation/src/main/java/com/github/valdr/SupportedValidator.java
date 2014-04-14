package com.github.valdr;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import lombok.Getter;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum SupportedValidator {

  Required(NotNull.class), Min(Min.class), Max(Max.class), Size(Size.class), Digits(Digits.class),
  Pattern(Pattern.class), Future(Future.class), Past(Past.class);

  @Getter
  private final Class<? extends Annotation> beanValidationAnnotation;

  private SupportedValidator(Class<? extends Annotation> beanValidationAnnotation) {
    this.beanValidationAnnotation = beanValidationAnnotation;
  }

  public static Iterable<Class<? extends Annotation>> getAllBeanValidationAnnotations() {
    return Iterables.transform(Arrays.asList(values()), new Function<SupportedValidator, Class<? extends Annotation>>() {

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
