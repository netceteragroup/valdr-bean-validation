package com.netcetera.valdrjsr303.model.c;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TestModelWithASingleAnnotatedMemberWithCustomMessageKey {
  @NotNull(message = "paul")
  private String notNullString;
}
