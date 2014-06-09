package com.github.valdr;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

/**
 * Tests ValidationRulesServlet.
 */
public class ValidationRulesServletTest {

  private final ValidationRulesServlet servlet = new ValidationRulesServlet();
  private ServletConfig servletConfig;

  @Before
  public void setUp() throws Exception {
    servletConfig = mock(ServletConfig.class);
  }

  /**
   * See method name.
   */
  @Test
  public void shouldThrowExceptionIfNoConfigFileExists  () throws ServletException {
    // given
    given(servletConfig.getInitParameter("configFile")).willReturn("");

    // when
    try {
      servlet.init(servletConfig);
      fail("Should throw IllegalArgumentException if no config file exists.");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  /**
   * See method name.
   */
  @Test
  public void shouldUseCustomConfigFileIfProvided() throws IOException, ServletException {
    // given
    String configFile = createTempFile("{\n" +
      "  \"modelPackageNames\": [\"com.github.valdr.model.b\"],\n" +
      "  \"corsAllowOriginPattern\": \"abc.com\"\n" +
      "}");

    given(servletConfig.getInitParameter("configFile")).willReturn(configFile);

    // when
    servlet.init(servletConfig);

    // then
    assertThat(ReflectionTestUtils.getField(servlet, "correctlyConfigured").toString(), is("true"));
    assertThat(ReflectionTestUtils.getField(servlet, "corsAllowOriginPattern").toString(), is("abc.com"));
  }

  private String createTempFile(String string) throws IOException {
    File tempFile = File.createTempFile("valdr", "json");
    FileWriter writer = new FileWriter(tempFile);
    IOUtils.write(string, writer);
    IOUtils.closeQuietly(writer);
    return tempFile.getAbsolutePath();
  }
}
