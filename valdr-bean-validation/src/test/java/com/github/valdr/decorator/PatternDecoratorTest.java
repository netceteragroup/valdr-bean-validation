package com.github.valdr.decorator;

import com.github.valdr.ConstraintAttributes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link PatternDecorator}.
 */
public class PatternDecoratorTest {

  private ConstraintAttributes constraintAttributes;

  /**
   * See method name.
   */
  @Test
  public void shouldStoreThePatternForValueAttribute() {
    /*
     * jakarta.validation.constraints.Pattern uses the attribute 'regex' to define the pattern. However, for valdr the
     * pattern must be passed in the 'value' attribute.
     */
    // given
    regexPattern("abc");
    PatternDecorator decorator = new PatternDecorator(constraintAttributes);
    // when
    Set<Map.Entry<String, Object>> decoratedEntries = decorator.entrySet();
    // then
    assertThat(firstKeyFrom(decoratedEntries), is("value"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldAddSlashPrefixSuffix() {
    // given
    regexPattern("abc");
    PatternDecorator decorator = new PatternDecorator(constraintAttributes);
    // when
    Set<Map.Entry<String, Object>> decoratedEntries = decorator.entrySet();
    // then
    assertThat(firstValueFrom(decoratedEntries), is("/abc/"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldAddSlashPrefixSuffixForPatternStartingWithSlash() {
    // given
    regexPattern("/abc");
    PatternDecorator decorator = new PatternDecorator(constraintAttributes);
    // when
    Set<Map.Entry<String, Object>> decoratedEntries = decorator.entrySet();
    // then
    assertThat(firstValueFrom(decoratedEntries), is("//abc/"));
  }

  private String firstValueFrom(Set<Map.Entry<String, Object>> decoratedEntries) {
    return decoratedEntries.iterator().next().getValue().toString();
  }

  private String firstKeyFrom(Set<Map.Entry<String, Object>> decoratedEntries) {
    return decoratedEntries.iterator().next().getKey();
  }

  private void regexPattern(String regexPattern) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("regexp", regexPattern);
    constraintAttributes = mock(ConstraintAttributes.class);
    given(constraintAttributes.entrySet()).willReturn(attributes.entrySet());
  }
}
