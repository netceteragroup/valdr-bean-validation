package com.netcetera.valdrjsr303;

import lombok.Value;

import java.util.List;

@Value
public class ParserConfiguration {
  List<String> modelPackageNames;
  List<String> customValidatorClassNames;
}
