package com.netcetera.valdrjsr303.model.a;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TestModelWithASingleAnnotatedMember {
  @NotNull
  private String notNullString;
}
