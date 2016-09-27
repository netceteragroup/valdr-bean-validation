package com.github.valdr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.valdr.model.i.TestModelWithoutAnyConstraintAnnotations;
import com.github.valdr.model.a.TestModelWithASingleAnnotatedMember;
import com.github.valdr.model.b.TestModelWithCustomValidator;
import com.github.valdr.model.c.TestModelWithASingleAnnotatedMemberWithCustomMessageKey;
import com.github.valdr.model.d.SubClassWithNoValidatedMembers;
import com.github.valdr.model.d.SuperClassWithValidatedMember;
import com.github.valdr.model.e.TestModelClassWithLotsOfIrrelevantAnnotations;
import com.github.valdr.model.f.TestModelWithHibernateEmailAnnotation;
import com.github.valdr.model.g.TestModelWithHibernateUrlAnnotation;
import com.github.valdr.model.h.TestModelWithPatterns;
import com.github.valdr.model.j.TestModelWithValidationGroups;
import com.github.valdr.model.validation.CustomValidation;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class ConstraintParserTest {
  private static final String LS = System.getProperty("line.separator");
  private ConstraintParser parser;

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
  public void shouldReturnEmptyJsonObjectWhenClassHasNoConstraintAnnotations() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithoutAnyConstraintAnnotations.class.getPackage().getName()),
      emptyStringList());
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
    parserConfiguredFor(Lists.newArrayList(TestModelWithASingleAnnotatedMember.class.getPackage().getName()),
      emptyStringList());
    // when
    String json = parser.parse();
    // then
    String expected = "{" + LS +
      "  \"" + TestModelWithASingleAnnotatedMember.class.getSimpleName() + "\" : {" + LS +
      "    \"notNullString\" : {" + LS +
      "      \"required\" : {" + LS +
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
    parserConfiguredFor(Lists.newArrayList(TestModelWithASingleAnnotatedMemberWithCustomMessageKey.class.getPackage()
      .getName()), emptyStringList());
    // when
    String json = parser.parse();
    // then
    String expected = "{" + LS +
      "  \"" + TestModelWithASingleAnnotatedMemberWithCustomMessageKey.class.getSimpleName() + "\" : {" + LS +
      "    \"notNullString\" : {" + LS +
      "      \"required\" : {" + LS +
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
  public void shouldIgnoreNotConfiguredCustomAnnotations() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithCustomValidator.class.getPackage().getName()),
      emptyStringList());
    // when
    String json = parser.parse();
    // then
    assertThat(json, is("{ }"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldIgnoreNotSupportedAnnotations() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelClassWithLotsOfIrrelevantAnnotations.class.getPackage().getName()
    ), emptyStringList());
    // when
    String json = parser.parse();
    // then
    assertThat(json, is("{ }"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldProcessConfiguredCustomAnnotation() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithCustomValidator.class.getPackage().getName()), Lists
      .newArrayList(CustomValidation.class.getName()));
    // when
    String json = parser.parse();
    // then
    String expected = "{" + LS +
      "  \"" + TestModelWithCustomValidator.class.getSimpleName() + "\" : {" + LS +
      "    \"customValidation\" : {" + LS +
      "      \"" + CustomValidation.class.getName() + "\" : { }" + LS +
      "    }" + LS +
      "  }" + LS +
      "}";
    assertThat(json, is(expected));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldSupportHibernateEmailAnnotation() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithHibernateEmailAnnotation.class.getPackage().getName()),
      emptyStringList());
    // when
    String json = parser.parse();
    // then
    assertThat(json, containsString(TestModelWithHibernateEmailAnnotation.class.getSimpleName()));
    assertThat(json, containsString("hibernateEmail"));
    assertThat(json, containsString("{org.hibernate.validator.constraints.Email.message}"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldSupportHibernateUrlAnnotation() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithHibernateUrlAnnotation.class.getPackage().getName()),
      emptyStringList());
    // when
    String json = parser.parse();
    // then
    String expected = "{" + LS +
      "  \"" + TestModelWithHibernateUrlAnnotation.class.getSimpleName() + "\" : {" + LS +
      "    \"url\" : {" + LS +
      "      \"hibernateUrl\" : {" + LS;
    assertThat(json, startsWith(expected));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldConsiderSuperClassMembers() throws IOException {
    // given
    parserConfiguredFor(Lists.newArrayList(SubClassWithNoValidatedMembers.class.getPackage().getName()),
      emptyStringList());
    // when
    String json = parser.parse();
    JsonNode jsonNode = new ObjectMapper().readTree(json);
    // then
    assertThat(jsonNode.get(SuperClassWithValidatedMember.class.getSimpleName()).get("notNullString").get("required")
      .get("message").asText(), is("{javax.validation.constraints.NotNull.message}"));
    assertThat(jsonNode.get(SubClassWithNoValidatedMembers.class.getSimpleName()).get("notNullString").get
      ("required").get("message").asText(), is("{javax.validation.constraints.NotNull.message}"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldDecoratePatternConstraint() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithPatterns.class.getPackage().getName()), emptyStringList());
    // when
    String json = parser.parse();
    // then
    assertThat(json, containsString("/abc/"));
    assertThat(json, containsString("/\\\\abc\\\\./")); // JSON needs to escape \ -> double escape here
    assertThat(json, containsString("/\\\\\\\\abc\\\\./")); // JSON needs to escape \ -> double escape here
  }

  @Test
  public void shouldIncludeValidationGroupWhenSoConfigured() {
    // given
    parserConfiguredFor(Lists.newArrayList(TestModelWithValidationGroups.class.getPackage().getName()), emptyStringList(), true);
    // when
    String json = parser.parse();
    // then
    String expected = "{\n" +
            "  \"TestModelWithValidationGroups\" : {\n" +
            "    \"bar\" : {\n" +
            "      \"required\" : {\n" +
            "        \"message\" : \"{javax.validation.constraints.NotNull.message}\",\n" +
            "        \"groups\" : [ \"GroupTwo\", \"GroupOne\" ]\n" +
            "      }\n" +
            "    },\n" +
            "    \"foo\" : {\n" +
            "      \"required\" : {\n" +
            "        \"message\" : \"{javax.validation.constraints.NotNull.message}\",\n" +
            "        \"groups\" : [ \"GroupOne\" ]\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
    assertThat(json, is(expected));
  }


  private void parserConfiguredFor(List<String> modelPackages, List<String> customAnnotationClasses) {
    Options options = new Options();
    options.setModelPackages(modelPackages);
    options.setCustomAnnotationClasses(customAnnotationClasses);
    parser = new ConstraintParser(options);
  }

  private void parserConfiguredFor(List<String> modelPackages, List<String> customAnnotationClasses, boolean outputValidationGroups) {
    Options options = new Options();
    options.setModelPackages(modelPackages);
    options.setCustomAnnotationClasses(customAnnotationClasses);
    options.setOutputValidationGroups(outputValidationGroups);
    parser = new ConstraintParser(options);
  }

  private List<String> emptyStringList() {
    return Collections.emptyList();
  }
}
