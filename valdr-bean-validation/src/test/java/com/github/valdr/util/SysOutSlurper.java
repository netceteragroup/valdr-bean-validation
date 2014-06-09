package com.github.valdr.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Utility class to capture output sent to sys out. Anything that is sent to sys out after this slurper has been
 * activated is captured and returned once it is deactivated.
 */
public class SysOutSlurper {
  private PrintStream originalSysOut;
  private ByteArrayOutputStream slurpingSysOut;

  /**
   * Activates sys out redirection.
   */
  public void activate() {
    slurpingSysOut = new ByteArrayOutputStream();
    originalSysOut = System.out;
    System.setOut(new PrintStream(slurpingSysOut));
  }

  /**
   * Resets sys out and returns captured string.
   *
   * @return captured string
   */
  public String deactivate() {
    System.out.flush();
    System.setOut(originalSysOut);
    String s = slurpingSysOut.toString();
    System.out.println(s);
    return s;
  }
}
