package com.github.valdr.model.j;

import com.github.valdr.util.ValidationGroup;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TestModelWithValidationGroups {
    @NotNull(groups = ValidationGroup.GroupOne.class)
    private String foo;

    @NotNull(groups = {ValidationGroup.GroupTwo.class, ValidationGroup.GroupOne.class})
    private String bar;
}
