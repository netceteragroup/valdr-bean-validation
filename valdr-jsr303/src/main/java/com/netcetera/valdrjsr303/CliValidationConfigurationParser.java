package com.netcetera.valdrjsr303;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class CliValidationConfigurationParser {
  public static void main(String[] args) {
    ParserConfiguration parserConfiguration = new ParserConfiguration(toList(args[1]), toList(args[1]));
    System.out.println(new ValidationConfigurationParser(parserConfiguration).parse());
  }

  private static List<String> toList(String arg) {
    return Arrays.asList(StringUtils.split(arg, ','));
  }
}
