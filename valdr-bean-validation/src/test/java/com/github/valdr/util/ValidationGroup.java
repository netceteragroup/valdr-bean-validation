package com.github.valdr.util;

import javax.validation.groups.Default;

public class ValidationGroup {
    public interface GroupOne extends Default {
        // validation group marker interface
    }

  public interface GroupOnePointOne extends GroupOne {
    // validation group marker interface
  }

  public interface GroupTwo extends Default {
      // validation group marker interface
  }

  // Should not show up as does not extend Default
  public interface GroupThreeDoesNotExtendDefault {
    // validation group marker interface
  }
}