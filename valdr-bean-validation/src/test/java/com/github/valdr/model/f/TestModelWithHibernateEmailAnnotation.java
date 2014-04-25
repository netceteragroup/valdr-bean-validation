package com.github.valdr.model.f;

import org.hibernate.validator.constraints.Email;

public class TestModelWithHibernateEmailAnnotation {
  @Email
  private String email;
}
