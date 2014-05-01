package com.github.valdr;

import lombok.Value;

import java.util.List;

/**
 * Simple config bean.
 */
@Value
public class ParserConfiguration {
  private List<String> modelPackageNames;
  private List<String> customAnnotationClassNames;
}
