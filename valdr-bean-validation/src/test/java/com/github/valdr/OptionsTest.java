package com.github.valdr;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests Options.
 */
public class OptionsTest {
  /**
   * See method name.
   */
  @Test
  public void shouldComplainAboutUnconfiguredModelPackageNames() {
    // given
    Options options = new Options();
    // when
    try {
      options.validate();
      fail("Empty model packages configuration not allowed");
    } catch (Options.InvalidConfigurationException e) {
      // then
      assertThat(e.getMessage(), is("Model package names must not be empty."));
    }
  }
}
