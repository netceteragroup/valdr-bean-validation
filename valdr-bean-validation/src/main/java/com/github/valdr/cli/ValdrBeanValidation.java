package com.github.valdr.cli;

import com.github.valdr.ParserConfiguration;
import com.github.valdr.ValidationRulesParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Command line client to print the Bean Validation JSON model to system out or a defined output file. Usage is as
 * follows:
 * <pre>
 * java ValdrBeanValidation [-c <arg>] [-o <arg>] -p <arg>
 * -c <arg>   comma-separated list of fully qualified class names of custom
 * Bean Validation annotations
 * -o <arg>   output file to which the validation meta-model (JSON) is
 * written creating missing folders automatically, if omitted the
 * output is sent to system out
 * -p <arg>   comma-separated list of fully qualified names of packages in
 * which you keep Bean Validation annotated model classes
 * </pre>
 */
public final class ValdrBeanValidation {

  private ValdrBeanValidation() {
    // utility class
  }

  /**
   * See class comment.
   *
   * @param args cli arguments
   */
  public static void main(String[] args) {
    Options cliOptions = createCliOptions();
    try {
      CommandLine cli = parseCli(args, cliOptions);
      List<String> modelPackages = toList(cli.getOptionValue("p"));
      List<String> customValidatorClassNames = toList(cli.getOptionValue("c"));
      String outputFile = cli.getOptionValue("o");

      ParserConfiguration parserConfiguration = new ParserConfiguration(modelPackages, customValidatorClassNames);
      ValidationRulesParser parser = new ValidationRulesParser(parserConfiguration);
      try {
        output(parser, outputFile);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } catch (IncompleteCliException e) {
      // If the command line is not complete just print usage and help
      printErrorWithUsageAndHelp(cliOptions);
    }
  }

  private static void printErrorWithUsageAndHelp(Options cliOptions) {
    System.out.println("Error. Not all mandatory args provided.");
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("java " + ValdrBeanValidation.class.getSimpleName(), cliOptions, true);
  }

  private static CommandLine parseCli(String[] args, Options options) throws IncompleteCliException {
    GracefulCliParser parser = new GracefulCliParser();
    try {
      CommandLine commandLine = parser.parse(options, args);
      if (parser.isIncomplete()) {
        throw new IncompleteCliException();
      } else {
        return commandLine;
      }
    } catch (ParseException e) {
      throw new RuntimeException("Failed to parse command line.", e);
    }
  }

  private static Options createCliOptions() {
    Options options = new Options();
    Option packageNameOption = new Option("p", true, "comma-separated list of fully qualified names of packages "
      + "in which you keep JSR 303 annotated model classes");
    packageNameOption.setRequired(true);
    options.addOption(packageNameOption);
    options.addOption("c", true,
      "comma-separated list of fully qualified class names of custom JSR 303 " + "annotations");
    options.addOption("o", true, "output file to which the validation meta-model (JSON) is written, "
      + "if omitted the output is sent to system out");
    return options;
  }

  private static void output(ValidationRulesParser parser, String outputFile) throws IOException {
    String output = parser.parse();
    if (StringUtils.isEmpty(outputFile)) {
      System.out.println(output);
    } else {
      File file = new File(outputFile);
      file.getParentFile().mkdirs();
      try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8")) {
        writer.write(output);
      }
    }
  }

  private static List<String> toList(String commaSeparatedArg) {
    if (StringUtils.isEmpty(commaSeparatedArg)) {
      return Collections.emptyList();
    } else {
      return Arrays.asList(StringUtils.split(commaSeparatedArg, ','));
    }
  }
}
