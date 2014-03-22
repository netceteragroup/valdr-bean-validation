package com.netcetera.valdrjsr303.cli;

import lombok.Getter;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.util.ListIterator;

/**
 * The {@link BasicParser} throws an exception while parsing when a required option or arg is missing in the command
 * line. This parser swallows such errors. However, once it's done you can call {@link GracefulCliParser#isIncomplete
 * ()} to find out if such a swallowed error did in fact occur.
 */
public class GracefulCliParser extends BasicParser {
  @Getter
  private boolean incomplete;

  @Override
  protected void checkRequiredOptions() {
    try {
      super.checkRequiredOptions();
    } catch (MissingOptionException e) {
      incomplete = true;
    }
  }

  @Override
  public void processArgs(Option opt, ListIterator iter) {
    try {
      super.processArgs(opt, iter);
    } catch (ParseException e) {
      incomplete = true;
    }
  }
}