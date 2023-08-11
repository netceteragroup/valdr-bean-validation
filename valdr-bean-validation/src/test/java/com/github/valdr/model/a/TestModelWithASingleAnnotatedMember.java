package com.github.valdr.model.a;

import lombok.Getter;

import jakarta.validation.constraints.NotNull;

@Getter
public class TestModelWithASingleAnnotatedMember {
  @NotNull
  private String notNullString;
}
