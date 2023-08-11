package com.github.valdr.model.e;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

public class TestModelClassWithLotsOfIrrelevantAnnotations {
  @Deprecated
  @Getter
  @JsonIgnore
  private String foo;
}
