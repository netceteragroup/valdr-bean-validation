package com.github.valdr.decorator;

import com.github.valdr.ValidationRuleAttributes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link PatternDecorator}.
 */
public class PatternDecoratorTest {

  private ValidationRuleAttributes constraintAttributes;

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

  private void regexPattern(String regexPattern) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("regexp", regexPattern);
    constraintAttributes = mock(ValidationRuleAttributes.class);
    given(constraintAttributes.entrySet()).willReturn(attributes.entrySet());
  }
}
