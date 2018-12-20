package com.github.valdr;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

/**
 * Provides means to scan the classpath for model classes that need to be parsed for constraint annotations.
 */
public class ClasspathScanner {
  private final Options options;

  /**
   * Constructor.
   *
   * @param options the only relevant input for the parser is this configuration
   */
  public ClasspathScanner(Options options) {
    this.options = options;
  }

  /**
   * Scans the classpath to find all classes that are in the configured model packages. It ignores excluded classes.
   *
   * @return classes to parse
   * @see Options
   */
  public Set<Class<?>> findClassesToParse() {
    Reflections reflections = new Reflections(new ConfigurationBuilder().
      setUrls(buildClassLoaderUrls()).setScanners(new SubTypesScanner(false)).filterInputsBy(buildPackagePredicates()));

    return reflections.getSubTypesOf(Object.class);
  }

  private Collection<URL> buildClassLoaderUrls() {
    Collection<URL> urls = Sets.newHashSet();
    for (String packageName : options.getModelPackages()) {
      if (StringUtils.isNotEmpty(packageName)) {
        urls.addAll(ClasspathHelper.forPackage(packageName));
      }
    }
    return urls;
  }

  private Predicate<String> buildPackagePredicates() {
    FilterBuilder filterBuilder = new FilterBuilder();
    // Include package names
    for (String packageName : options.getModelPackages()) {
      filterBuilder.include(FilterBuilder.prefix(packageName));
    }
    // Exclude class names
    for (String excludedClassName : options.getExcludedClasses()) {
      filterBuilder.exclude("^" + StringUtils.replace(excludedClassName, ".", "\\.") + "\\.class$");
    }
    return filterBuilder;
  }
}
