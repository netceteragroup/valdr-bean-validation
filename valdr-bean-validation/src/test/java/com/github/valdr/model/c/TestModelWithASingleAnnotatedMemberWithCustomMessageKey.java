package com.github.valdr.model.c;

import lombok.Getter;

import jakarta.validation.constraints.NotNull;

@Getter
public class TestModelWithASingleAnnotatedMemberWithCustomMessageKey {
  @NotNull(message = "paul")
  private String notNullString;
}
