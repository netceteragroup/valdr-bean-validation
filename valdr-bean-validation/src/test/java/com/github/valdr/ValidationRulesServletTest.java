package com.github.valdr;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

import static org.hamcrest.Matchers.containsString;
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
  private MockHttpServletResponse response;
  private MockHttpServletRequest request;

  @Before
  public void setUp() throws Exception {
    servletConfig = mock(ServletConfig.class);
    response = new MockHttpServletResponse();
    request = new MockHttpServletRequest();
  }

  /**
   * See method name.
   */
  @Test
  public void shouldThrowExceptionIfNoConfigFileExists() throws ServletException {
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
    givenThisConfiguration("{\n" +
      "  \"modelPackages\": [\"com.github.valdr.model.b\"],\n" +
      "  \"corsAllowOriginPattern\": \"abc.com\"\n" +
      "}");

    // when
    servlet.init(servletConfig);

    // then
    assertThat(ReflectionTestUtils.getField(servlet, "correctlyConfigured").toString(), is("true"));
    assertThat(ReflectionTestUtils.getField(servlet, "corsAllowOriginPattern").toString(), is("abc.com"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldSendErrorIfNotCorrectlyConfigured() throws IOException, ServletException {
    // given mandatory 'modelPackages' is missing
    givenThisConfiguration("{\n" +
      "  \"corsAllowOriginPattern\": \"abc.com\"\n" +
      "}");
    servlet.init(servletConfig);

    // when
    servlet.doGet(request, response);

    // then
    assertThat(response.getStatus(), is(500));
    assertThat(response.getErrorMessage(), containsString("Invalid configuration"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldSendHttp200IfAllIsWell() throws IOException, ServletException {
    // given
    givenThisConfiguration("{\n" +
      "  \"modelPackages\": [\"com.github.valdr.model.b\"],\n" +
      "  \"corsAllowOriginPattern\": \"abc.com\"\n" +
      "}");
    servlet.init(servletConfig);

    // when
    servlet.doGet(request, response);

    // then
    assertThat(response.getStatus(), is(200));
  }

  private void givenThisConfiguration(String jsonString) throws IOException {
    String configFile = createTempFile(jsonString);
    given(servletConfig.getInitParameter("configFile")).willReturn(configFile);
  }

  private String createTempFile(String string) throws IOException {
    File tempFile = File.createTempFile("valdr", "json");
    FileWriter writer = new FileWriter(tempFile);
    writer.write(string);
    writer.close();
    return tempFile.getAbsolutePath();
  }
}
