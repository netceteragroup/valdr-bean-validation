package com.github.valdr;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import lombok.Getter;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum SupportedValidators {

  Required(NotNull.class), Min(Min.class), Max(Max.class), Size(Size.class), Digits(Digits.class),
  Pattern(Pattern.class), Future(Future.class), Past(Past.class);

  @Getter
  private final Class<? extends Annotation> beanValidationAnnotation;

  private SupportedValidators(Class<? extends Annotation> beanValidationAnnotation) {
    this.beanValidationAnnotation = beanValidationAnnotation;
  }

  public static Iterable<Class<? extends Annotation>> getAllBeanValidationAnnotations() {
    return Iterables.transform(Arrays.asList(values()), new Function<SupportedValidators, Class<? extends Annotation>>() {

      @Override
      public Class<? extends Annotation> apply(SupportedValidators input) {
        return input.getBeanValidationAnnotation();
      }
    });
  }

  public static SupportedValidators valueOfAnnotationClassOrNull(Class<? extends Annotation> beanValidationAnnotation) {
    for (SupportedValidators supportedValidator : values()) {
      if (supportedValidator.getBeanValidationAnnotation().equals(beanValidationAnnotation)) {
        return supportedValidator;
      }
    }
    return null;
  }
}
