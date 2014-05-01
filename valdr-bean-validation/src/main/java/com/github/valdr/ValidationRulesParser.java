package com.github.valdr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.valdr.serializer.AttributeMapSerializer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Parses classes in defined packages for supported <a href="http://beanvalidation.org/">Bean Validation (JSR 303)</a>
 * annotations ({@code javax.validation.*}) and configured custom annotations. The parsing result is a
 * JSON string that complies with the document specified by <a href="https://github.com/netceteragroup/valdr">valdr</a>.
 *
 * @see SupportedValidator
 * @see ParserConfiguration
 */
public class ValidationRulesParser {
  private final Logger logger = LoggerFactory.getLogger(ValidationRulesParser.class);

  private final ParserConfiguration parserConfiguration;
  private final Iterable<Class<? extends Annotation>> allRelevantAnnotationClasses;

  /**
   * Constructor.
   *
   * @param parserConfiguration the only relevant input for the parser is this configuration
   */
  public ValidationRulesParser(ParserConfiguration parserConfiguration) {
    this.parserConfiguration = parserConfiguration;
    allRelevantAnnotationClasses = Iterables.concat(SupportedValidator.getAllBeanValidationAnnotations(),
      getConfiguredCustomAnnotations());
  }

  /**
   * Based on the configuration passed to the constructor model classes are parsed for validation rules.
   *
   * @return JSON string for <a href="https://github.com/netceteragroup/valdr">valdr</a>
   */
  public String parse() {
    Map<String, ClassValidationRules> classNameToValidationRulesMap = new HashMap<>();

    for (Class clazz : findClassesToParse()) {
      if (clazz != null) {
        ClassValidationRules classValidationRules = new AnnotatedClass(clazz,
          allRelevantAnnotationClasses).extractValidationRules();
        if (classValidationRules.size() > 0) {
          classNameToValidationRulesMap.put(clazz.getSimpleName(), classValidationRules);
        }
      }
    }

    return toJson(classNameToValidationRulesMap);
  }

  private String toJson(Map<String, ClassValidationRules> classNameToValidationRulesMap) {
    ObjectMapper objectMapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(AttributeMap.class, new AttributeMapSerializer());
    objectMapper.registerModule(module);

    ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(classNameToValidationRulesMap);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Set<Class<?>> findClassesToParse() {
    Reflections reflections = new Reflections(new ConfigurationBuilder().
      setUrls(buildClassLoaderUrls()).setScanners(new SubTypesScanner(false)).filterInputsBy(buildPackagePredicates()));

    return reflections.getSubTypesOf(Object.class);
  }

  private Iterable<? extends Class<? extends Annotation>> getConfiguredCustomAnnotations() {
    return Iterables.transform(parserConfiguration.getCustomAnnotationClassNames(), new Function<String,
      Class<? extends Annotation>>() {
      @Override
      @SuppressWarnings("unchecked")
      public Class<? extends Annotation> apply(String className) {
        Class<?> validatorClass = Reflections.forName(className);
        if (validatorClass.isAnnotation()) {
          return (Class<? extends Annotation>) validatorClass;
        } else {
          logger.warn("The configured custom annotation class '{}' is not an annotation. It will be ignored.",
            validatorClass);
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
}
