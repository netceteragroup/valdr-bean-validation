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
import java.util.*;

/**
 * Provides means to scan the classpath for model classes that need to be parsed for constraint annotations.
 */
public class ClasspathScanner {
  private static final Logger logger = LoggerFactory.getLogger(ClasspathScanner.class);
  private final Options options;

  private static Reflections all = null;


  /**
   * Constructor.
   *
   * @param options the only relevant input for the parser is this configuration
   */
  public ClasspathScanner(Options options) {
    this.options = options;

    List<String> groupsToScan = new ArrayList(options.getValidationGroupPackages());
    if (groupsToScan.size() > 0) {
      groupsToScan.add("javax.validation.groups");

      all = new Reflections(new ConfigurationBuilder().
              setUrls(buildClassLoaderUrls(groupsToScan)).setScanners(new SubTypesScanner(false)));

    }

  }

  /**
   * Scans the classpath to find all classes that are in the configured model packages. It ignores excluded classes.
   *
   * @return classes to parse
   * @see com.github.valdr.Options#getModelPackages()
   * @see com.github.valdr.Options#getExcludedClasses()
   */
  public Set<Class<?>> findClassesToParse() {
    Reflections reflections = new Reflections(new ConfigurationBuilder().
      setUrls(buildClassLoaderUrls(options.getModelPackages())).setScanners(new SubTypesScanner(false)).filterInputsBy(buildPackagePredicates()));

    return reflections.getSubTypesOf(Object.class);
  }

  // Using a static method accessing a static member is a sneaky optimization to avoid
  // scanning through a lot of classes when analyzing each constraint annotation.
  // The member gets set when this class' constructor is called.
  public static Set<Class<?>> getSubTypesOf(Class aClass) {
    return all == null ? new HashSet<>() : all.getSubTypesOf(aClass);
  }

  private Collection<URL> buildClassLoaderUrls(List<String> packageNames) {
    Collection<URL> urls = Sets.newHashSet();
    for (String packageName : packageNames) {
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
