package com.github.valdr;

import com.google.common.collect.Lists;
import com.github.valdr.model.a.TestModelWithASingleAnnotatedMember;
import com.github.valdr.model.b.TestModelWithCustomValidator;
import com.github.valdr.model.c.TestModelWithASingleAnnotatedMemberWithCustomMessageKey;
import com.github.valdr.model.d.SubClassWithNoValidatedMembers;
import com.github.valdr.model.d.SuperClassWithValidatedMember;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ValidationRulesParserTest {
  private ValidationRulesParser parser;

  /**
   * See method name.
   */
  @Test
  public void shouldReturnEmptyJsonObjectWhenNoClassIsFound() {
    // given
    parserConfiguredFor(emptyStringList(), emptyStringList());
    // when
    String json = parser.parse();
    // then
    assertThat(json, is("{ }"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldReturnDefaultMessage() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithASingleAnnotatedMember.class.getPackage().getName()), emptyStringList());
    // when
    String json = parser.parse();
    // then
    String expected = "{\n" +
      "  \"" + TestModelWithASingleAnnotatedMember.class.getSimpleName() + "\" : {\n" +
      "    \"notNullString\" : {\n" +
      "      \"Required\" : {\n" +
      "        \"message\" : \"{javax.validation.constraints.NotNull.message}\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertThat(json, is(expected));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldReturnCustomMessage() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithASingleAnnotatedMemberWithCustomMessageKey.class.getPackage().getName()), emptyStringList());
    // when
    String json = parser.parse();
    // then
    String expected = "{\n" +
      "  \"" + TestModelWithASingleAnnotatedMemberWithCustomMessageKey.class.getSimpleName() + "\" : {\n" +
      "    \"notNullString\" : {\n" +
      "      \"Required\" : {\n" +
      "        \"message\" : \"paul\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertThat(json, is(expected));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldIgnoreNotConfiguredCustomValidators() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithCustomValidator.class.getPackage().getName()), emptyStringList());
    // when
    String json = parser.parse();
    // then
    assertThat(json, is("{ }"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldConsiderSuperClassMembers() {
    // given
    parserConfiguredFor(Lists.newArrayList(SubClassWithNoValidatedMembers.class.getPackage().getName()), emptyStringList());
    // when
    String json = parser.parse();
    // then
    String expected = "{\n" +
      "  \"" + SuperClassWithValidatedMember.class.getSimpleName() + "\" : {\n" +
      "    \"notNullString\" : {\n" +
      "      \"Required\" : {\n" +
      "        \"message\" : \"{javax.validation.constraints.NotNull.message}\"\n" +
      "      }\n" +
      "    }\n" +
      "  },\n" +
      "  \"" + SubClassWithNoValidatedMembers.class.getSimpleName() + "\" : {\n" +
      "    \"notNullString\" : {\n" +
      "      \"Required\" : {\n" +
      "        \"message\" : \"{javax.validation.constraints.NotNull.message}\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertThat(json, is(expected));
  }

  private void parserConfiguredFor(List<String> modelPackageNames, List<String> customValidatorClassNames) {
    parser = new ValidationRulesParser(new ParserConfiguration(modelPackageNames, customValidatorClassNames));
  }

  private List<String> emptyStringList(){
    return Collections.emptyList();
  }
}
