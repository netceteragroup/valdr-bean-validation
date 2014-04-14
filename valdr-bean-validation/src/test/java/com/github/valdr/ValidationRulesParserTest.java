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
  private static final String LS = System.getProperty("line.separator");
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
    String expected = "{" + LS +
      "  \"" + TestModelWithASingleAnnotatedMember.class.getSimpleName() + "\" : {" + LS +
      "    \"notNullString\" : {" + LS +
      "      \"Required\" : {" + LS +
      "        \"message\" : \"{javax.validation.constraints.NotNull.message}\"" + LS +
      "      }" + LS +
      "    }" + LS +
      "  }" + LS +
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
    String expected = "{" + LS +
      "  \"" + TestModelWithASingleAnnotatedMemberWithCustomMessageKey.class.getSimpleName() + "\" : {" + LS +
      "    \"notNullString\" : {" + LS +
      "      \"Required\" : {" + LS +
      "        \"message\" : \"paul\"" + LS +
      "      }" + LS +
      "    }" + LS +
      "  }" + LS +
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
    String expected = "{" + LS +
      "  \"" + SuperClassWithValidatedMember.class.getSimpleName() + "\" : {" + LS +
      "    \"notNullString\" : {" + LS +
      "      \"Required\" : {" + LS +
      "        \"message\" : \"{javax.validation.constraints.NotNull.message}\"" + LS +
      "      }" + LS +
      "    }" + LS +
      "  }," + LS +
      "  \"" + SubClassWithNoValidatedMembers.class.getSimpleName() + "\" : {" + LS +
      "    \"notNullString\" : {" + LS +
      "      \"Required\" : {" + LS +
      "        \"message\" : \"{javax.validation.constraints.NotNull.message}\"" + LS +
      "      }" + LS +
      "    }" + LS +
      "  }" + LS +
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
