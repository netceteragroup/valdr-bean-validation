package com.github.valdr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.valdr.serializer.MinimalMapSerializer;
import com.google.common.collect.Iterables;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p> Parses classes in defined packages for supported <a href="http://beanvalidation.org/">Bean Validation (JSR
 * 303)</a>
 * annotations ({@code jakarta.validation.*}) and configured custom annotations.
 * <p> The parsing result is a JSON string that complies with the document specified by <a
 * href="https://github.com/netceteragroup/valdr">valdr</a>.
 *
 * @see BuiltInConstraint
 * @see Options
 */
public class ConstraintParser {
  private final Logger logger = LoggerFactory.getLogger(ConstraintParser.class);

  private final ClasspathScanner classpathScanner;
  private final Iterable<Class<? extends Annotation>> allRelevantAnnotationClasses;
  private final Options options;

  /**
   * Constructor.
   *
   * @param options the only relevant input for the parser is this configuration
   */
  public ConstraintParser(Options options) {
    this.options = options;
    this.classpathScanner = new ClasspathScanner(options);
    allRelevantAnnotationClasses = Iterables.concat(BuiltInConstraint.getAllBeanValidationAnnotations(),
      getConfiguredCustomAnnotations());
  }

  /**
   * Based on the configuration passed to the constructor model classes are parsed for constraints.
   *
   * @return JSON string for <a href="https://github.com/netceteragroup/valdr">valdr</a>
   */
  public String parse() {
    Map<String, ClassConstraints> classNameToValidationRulesMap = new HashMap<>();

    for (Class clazz : classpathScanner.findClassesToParse()) {
      if (clazz != null) {
        ClassConstraints classValidationRules = new AnnotatedClass(clazz, options.getExcludedFields(),
          allRelevantAnnotationClasses).extractValidationRules();
        if (classValidationRules.size() > 0) {
          String name = options.getOutputFullTypeName() ? clazz.getName() : clazz.getSimpleName();
          classNameToValidationRulesMap.put(name, classValidationRules);
        }
      }
    }

    return toJson(classNameToValidationRulesMap);
  }

  @SneakyThrows(IOException.class)
  private String toJson(Map<String, ClassConstraints> classNameToValidationRulesMap) {
    ObjectMapper objectMapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(MinimalMap.class, new MinimalMapSerializer());
    objectMapper.registerModule(module);

    ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(classNameToValidationRulesMap);
  }

  @SuppressWarnings("unchecked")
  private Iterable<? extends Class<? extends Annotation>> getConfiguredCustomAnnotations() {
    return options.getCustomAnnotationClasses().stream().map(className -> {
      try {
        Class<?> validatorClass = Class.forName(className);
        if (!validatorClass.isAnnotation()) {
          logger.warn("The configured custom annotation class '{}' is not an annotation. It will be ignored.", validatorClass);
        } else {
          return (Class<? extends Annotation>) validatorClass;
        }
      } catch (ClassNotFoundException e) {
        logger.warn("The configured class '{}' can not be found. It will be ignored.", className);
      }
      return null;
    }).filter(Objects::nonNull).toList();
  }
}
