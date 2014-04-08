package com.github.valdr;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.github.valdr.thirdparty.spring.AnnotationUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class ValidationConfigurationParser {
  private final Logger logger = LoggerFactory.getLogger(ValidationConfigurationParser.class);

  private final ParserConfiguration parserConfiguration;
  private final Iterable<Class<? extends Annotation>> allRelevantAnnotationClasses;

  public ValidationConfigurationParser(ParserConfiguration parserConfiguration) {
    this.parserConfiguration = parserConfiguration;
    allRelevantAnnotationClasses = Iterables.concat(SupportedValidators.getAllBeanValidationAnnotations(),
      getConfiguredAnnotations());
  }

  public String parse() {
    Map<String, Map<String, Map<String, Map<String, Object>>>> classNameToValidationsMap = new HashMap<>();

    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(buildClassLoaderUrls()).setScanners(new
      SubTypesScanner(false)).filterInputsBy(buildPackagePredicates()));

    Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

    for (Class clazz : classes) {
      if (clazz != null) {
        Map<String, Map<String, Map<String, Object>>> validations = getValidations(clazz);
        if (validations.size() > 0) {
          classNameToValidationsMap.put(clazz.getSimpleName(), validations);
        }
      }
    }
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(classNameToValidationsMap);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public Map<String, Map<String, Map<String, Object>>> getValidations(Class clazz) {
    final Map<String, Map<String, Map<String, Object>>> fieldNameToValidationsMap = new HashMap<>();
    Set<Field> allFields = ReflectionUtils.getAllFields(clazz, buildAnnotationsPredicate());
    for (Field field : allFields) {
      Map<String, Map<String, Object>> annotationToAttributesMap = processAnnotatedField(field);
      if (annotationToAttributesMap.size() > 0) {
        fieldNameToValidationsMap.put(field.getName(), annotationToAttributesMap);
      }
    }
    return fieldNameToValidationsMap;
  }

  private Map<String, Map<String, Object>> processAnnotatedField(Field field) {
    Annotation[] annotations = field.getAnnotations();
    Map<String, Map<String, Object>> annotationToAttributesMap = new HashMap<>();
    for (Annotation annotation : annotations) {
      if (Iterables.contains(allRelevantAnnotationClasses, annotation.annotationType())) {
        Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
        removeUnusedAttributes(annotationAttributes);
        SupportedValidators supportedValidator = SupportedValidators.valueOfAnnotationClassOrNull(annotation.annotationType());
        annotationToAttributesMap.put(supportedValidator == null ? annotation.annotationType().getName() :
          supportedValidator.name(),
          annotationAttributes);
      }
    }

    return annotationToAttributesMap;
  }

  private Predicate<? super Field> buildAnnotationsPredicate() {
    Collection<Predicate<? super Field>> predicates = Lists.newArrayList();
    for (Class<? extends Annotation> annotationClass : allRelevantAnnotationClasses) {
      predicates.add(ReflectionUtils.withAnnotation(annotationClass));
    }
    return Predicates.or(predicates);
  }

  private Iterable<? extends Class<? extends Annotation>> getConfiguredAnnotations() {
    return Iterables.transform(parserConfiguration.getCustomValidatorClassNames(), new Function<String, Class<? extends Annotation>>() {
      @Override
      @SuppressWarnings("unchecked")
      public Class<? extends Annotation> apply(String className) {
        Class<?> validatorClass = Reflections.forName(className);
        if (validatorClass.isAnnotation()) {
          return (Class<? extends Annotation>) validatorClass;
        } else {
          logger.warn("The configured class '{}' is not an annotation. It will be ignored.", validatorClass);
          return null;
        }
      }
    });
  }

  private Collection<URL> buildClassLoaderUrls() {
    Collection<URL> urls = Sets.newHashSet();
    for (String packageName : parserConfiguration.getModelPackageNames()) {
      urls.addAll(ClasspathHelper.forPackage(packageName));
    }
    return urls;
  }

  private Predicate<String> buildPackagePredicates() {
    FilterBuilder filterBuilder = new FilterBuilder();
    for (String packageName : parserConfiguration.getModelPackageNames()) {
      filterBuilder.include(FilterBuilder.prefix(packageName));
    }
    return filterBuilder;
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
