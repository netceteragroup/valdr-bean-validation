package com.github.valdr;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * <p>Produces JSON validation rules on the fly parsing model classes in your classpath. Upon each request the
 * configured model packages in the classpath are parsed for classes containing supported or
 * custom Bean Validation annotations. The Servlet then builds and returns a JSON document with all validation rules
 * (i.e. Bean Validation constraints). The JSON document adheres to structure specified by valdr.</p>
 *
 * Servlet can be configured using following {@link javax.servlet.ServletConfig} init parameters (* = mandatory):
 * <ul>
 * <li>configFile: path to JSON configuration file, if omitted valdr-bean-validation.json is expected at root of class
 * path</li>
 * </ul>
 *
 * @see Options
 */
public class ValidationRulesServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(ValidationRulesServlet.class);
  private boolean correctlyConfigured = false;
  private String corsAllowOriginPattern;
  private String invalidConfigurationMessage;
  private ConstraintParser parser;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    Options options = loadOptions();
    invalidConfigurationMessage = validate(options);
    correctlyConfigured = StringUtils.isEmpty(invalidConfigurationMessage);
    corsAllowOriginPattern = options.getCorsAllowOriginPattern();

    parser = new ConstraintParser(options);

    logConfigurationStatus();
    logCorsStatus();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (correctlyConfigured) {
      String json = parser.parse();
      returnJson(response, json);
    } else {
      sendErrorInvalidConfiguration(response);
    }
  }

  private Options loadOptions() {
    InputStream inputStream = null;
    String configFile = getInitParameter("configFile");
    try {
      if (StringUtils.isEmpty(configFile)) {
        logger.info("Building parser configuration from default file path. Looking for '{}' in classpath.",
          "/" + Options.CONFIG_FILE_NAME);
        inputStream = ValidationRulesServlet.class.getResourceAsStream("/" + Options.CONFIG_FILE_NAME);
      } else {
        logger.info("Building parser configuration from configured file path '{}'.", configFile);
        inputStream = new FileInputStream(new File(configFile));
      }
      return new ObjectMapper().readValue(inputStream, Options.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot read config file.", e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

  private String validate(Options options) {
    String validationMsg = StringUtils.EMPTY;
    try {
      options.validate();
    } catch (Options.InvalidConfigurationException e) {
      validationMsg = "Invalid configuration: " + e.getMessage();
    }
    return validationMsg;
  }

  private void logConfigurationStatus() {
    if (correctlyConfigured) {
      logger.info("The Servlet appears to be correctly configured.");
    } else {
      logger.warn(invalidConfigurationMessage);
    }
  }

  private void logCorsStatus() {
    String logMsg = "Configured CORS allow-origin pattern is '{}'.";
    if (StringUtils.isEmpty(corsAllowOriginPattern)) {
      logMsg += " Therefore, not using CORS.";
    }
    logger.info(logMsg, corsAllowOriginPattern);
  }

  private void sendErrorInvalidConfiguration(HttpServletResponse response) throws IOException {
    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, invalidConfigurationMessage);
  }

  private void returnJson(HttpServletResponse response, String json) throws IOException {
    setCorsHeader(response);
    response.setContentType("application/json;charset=UTF-8");
    response.setContentLength(json.getBytes("utf-8").length);
    PrintWriter writer = response.getWriter();
    writer.write(json);
    writer.close();
  }

  private void setCorsHeader(HttpServletResponse response) {
    if (StringUtils.isNotEmpty(corsAllowOriginPattern)) {
      response.setHeader("Access-Control-Allow-Origin", corsAllowOriginPattern);
    }
  }
}
