package com.github.valdr.model.g;

import org.hibernate.validator.constraints.URL;

public class TestModelWithHibernateUrlAnnotation {
  @URL
  private String url;
}
