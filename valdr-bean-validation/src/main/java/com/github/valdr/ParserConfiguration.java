package com.github.valdr;

import lombok.Value;

import java.util.List;

@Value
public class ParserConfiguration {
  List<String> modelPackageNames;
  List<String> customValidatorClassNames;
}
