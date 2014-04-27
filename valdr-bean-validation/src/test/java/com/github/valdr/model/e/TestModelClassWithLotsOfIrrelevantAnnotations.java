package com.github.valdr.model.e;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.validator.constraints.SafeHtml;

public class TestModelClassWithLotsOfIrrelevantAnnotations {
  @Deprecated
  @SafeHtml
  @Getter
  @JsonIgnore
  private String foo;
}
