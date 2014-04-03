package com.github.valdr.model.d;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SuperClassWithValidatedMember {
  @NotNull()
  private String notNullString;
}
