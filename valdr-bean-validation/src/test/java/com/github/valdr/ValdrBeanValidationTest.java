package com.github.valdr;

import com.github.valdr.cli.ValdrBeanValidation;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ValdrBeanValidationTest {
  /**
   * See method name.
   */
  @Test
  public void shouldPrintErrorToSystemOutIfArgPIsMissing() {
    // given
    SysOutSlurper sysOutSlurper = new SysOutSlurper();
    sysOutSlurper.activate();
    String[] args = {""};

    // when
    ValdrBeanValidation.main(args);

    // then
    String sysOutContent = sysOutSlurper.deactivate();
    assertThat(sysOutContent, containsString("Error. "));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldPrintErrorToSystemOutIfArgPHasNoValue() {
    // given
    SysOutSlurper sysOutSlurper = new SysOutSlurper();
    sysOutSlurper.activate();
    String[] args = {"-p"};

    // when
    ValdrBeanValidation.main(args);

    // then
    String sysOutContent = sysOutSlurper.deactivate();
    assertThat(sysOutContent, containsString("Error. "));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldPrintToSystemOutIfNoOutputFileDefined() {
    // given
    SysOutSlurper sysOutSlurper = new SysOutSlurper();
    sysOutSlurper.activate();
    String[] args = {"-p", "com.foo.inexistent"};

    // when
    ValdrBeanValidation.main(args);

    // then
    String sysOutContent = sysOutSlurper.deactivate();
    assertThat(sysOutContent, is("{ }\n"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldPrintToOutputFileIfDefined() throws IOException {
    // given
    File tempFile = File.createTempFile("foo", "bar");
    String[] args = {"-p", "com.foo.inexistent", "-o", tempFile.getAbsolutePath()};

    // when
    ValdrBeanValidation.main(args);

    // then
    assertThat(FileUtils.readFileToString(tempFile), is("{ }"));
  }

  private static class SysOutSlurper {
    private PrintStream originalSysOut;
    private ByteArrayOutputStream slurpingSysOut;

    public void activate() {
      slurpingSysOut = new ByteArrayOutputStream();
      originalSysOut = System.out;
      System.setOut(new PrintStream(slurpingSysOut));
    }

    public String deactivate() {
      System.out.flush();
      System.setOut(originalSysOut);
      String s = slurpingSysOut.toString();
      System.out.println(s);
      return s;
    }
  }
}
