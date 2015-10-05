package com.github.valdr;

import com.github.valdr.model.a.TestModelWithASingleAnnotatedMember;
import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

/**
 * Tests ClasspathScanner.
 */
public class ClasspathScannerTest {

  /**
   * See method name.
   */
  @Test
  public void shouldFindNothingForEmptyPackageNames() {
    // given
    ClasspathScanner classpathScanner = scannerFor(emptyStringList(), emptyStringList());
    // when
    Set<Class<?>> classesToParse = classpathScanner.findClassesToParse();
    // then
    assertThat(classesToParse, is(empty()));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldFindNothingForEmptyPackageNameString() {
    // given
    ClasspathScanner classpathScanner = scannerFor(Lists.newArrayList(""), emptyStringList());
    // when
    Set<Class<?>> classesToParse = classpathScanner.findClassesToParse();
    // then
    assertThat(classesToParse, is(empty()));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldExcludedConfiguredClasses() {
    // given
    ClasspathScanner classpathScanner = scannerFor(Lists.newArrayList("com.github.valdr.model"),
      Lists.newArrayList("com.github.valdr.model.a.TestModelWithASingleAnnotatedMember"));
    // when
    Set<Class<?>> classesToParse = classpathScanner.findClassesToParse();
    // then
    assertThat(classesToParse, notContains(TestModelWithASingleAnnotatedMember.class));
    assertThat(classesToParse.size(), is(10));
  }

  private Matcher<? super Set<Class<?>>> notContains(final Class<?> testModelClass) {
    return new BaseMatcher() {
      @Override
      public boolean matches(Object item) {
        for (java.lang.Object o : (Set) item) {
          if (o.equals(testModelClass)) {
            return false;
          }
        }
        return true;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("notContains should not contain ").appendValue(testModelClass);
      }
    };
  }

  private ClasspathScanner scannerFor(List<String> modelPackages, List<String> excludedClasses) {
    Options options = new Options();
    options.setModelPackages(modelPackages);
    options.setExcludedClasses(excludedClasses);
    return new ClasspathScanner(options);
  }

  private List<String> emptyStringList() {
    return Collections.emptyList();
  }
}
