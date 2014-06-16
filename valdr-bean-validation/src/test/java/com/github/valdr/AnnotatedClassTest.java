package com.github.valdr;

import com.github.valdr.model.a.TestModelWithASingleAnnotatedMember;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class AnnotatedClassTest {

  /**
   * See method name.
   */
  @Test
  public void shouldNotReturnExcludedField() {
    // given
    AnnotatedClass annotatedClass = new AnnotatedClass(TestModelWithASingleAnnotatedMember.class, Lists.newArrayList(
      TestModelWithASingleAnnotatedMember.class.getName()
        + "#notNullString"), BuiltInConstraint.getAllBeanValidationAnnotations());
    // when
    ClassConstraints classConstraints = annotatedClass.extractValidationRules();
    // then
    assertThat(classConstraints.entrySet(), is(empty()));
  }
}
