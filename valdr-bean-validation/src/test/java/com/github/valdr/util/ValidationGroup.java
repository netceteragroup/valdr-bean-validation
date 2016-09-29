package com.github.valdr.util;

import javax.validation.groups.Default;

public class ValidationGroup {
    public interface GroupOne extends Default {
        // validation group marker interface
    }

    public interface GroupTwo {
        // validation group marker interface
    }
}