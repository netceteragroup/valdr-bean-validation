package com.github.valdr.model.h;

import javax.validation.constraints.Pattern;

public class TestModelWithPatterns {
  @Pattern(regexp = "abc")
  private String addSlashPrefixSuffix;
  @Pattern(regexp = "\\abc\\.") // \a matches the bell character ;-)
  private String withBackslashes;
  @Pattern(regexp = "\\\\abc\\.")
  private String withMoreBackslashes;
}
