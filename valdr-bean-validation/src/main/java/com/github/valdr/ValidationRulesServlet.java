package com.github.valdr;


import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidationRulesServlet extends HttpServlet {
  private static final String MODEL_PACKAGES_CONFIG_PARAM = "modelPackages";
  private static final String CUSTOM_VALIDATORS_CONFIG_PARAM = "customValidatorClassNames";
  private static final String CORS_PATTERN_CONFIG_PARAM = "corsAllowOriginPattern";
  private final String invalidConfigurationMessageIntro = "The Servlet is not configured correctly. ";
  private final String packageConfigurationMissingMessage =
    "The '" + MODEL_PACKAGES_CONFIG_PARAM + "' parameter in web.xml is missing.";
  private final Logger logger = LoggerFactory.getLogger(ValidationRulesServlet.class);
  private boolean correctlyConfigured = false;
  private String corsAllowOriginPattern;
  private String invalidConfigurationMessage;
  private ParserConfiguration parserConfiguration;
  private ValidationRulesParser parser;

  @Override
  public void init() throws ServletException {
    super.init();
    invalidConfigurationMessage = checkConfiguration();
    if (StringUtils.isEmpty(invalidConfigurationMessage)) {
      correctlyConfigured = true;
      logger.info("The Servlet appears to be correctly configured.");
      parserConfiguration = buildParserConfiguration();
      corsAllowOriginPattern = getCorsAllowOriginPattern();
      parser = new ValidationRulesParser(parserConfiguration);
    }
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

  private String getCorsAllowOriginPattern() {
    String value = getServletConfig().getInitParameter(CORS_PATTERN_CONFIG_PARAM);
    String logMsg = "Configured CORS allow-origin pattern is '{}'.";
    if (value == null) {
      value = StringUtils.EMPTY;
      logMsg += " Therefore, not using CORS.";
    }
    logger.info(logMsg, value);
    return value;
  }

  private ParserConfiguration buildParserConfiguration() {
    List<String> modelPackageNames = getServletConfigAsList(MODEL_PACKAGES_CONFIG_PARAM);
    List<String> customValidatorClassNames = getServletConfigAsList(CUSTOM_VALIDATORS_CONFIG_PARAM);
    ParserConfiguration config = new ParserConfiguration(modelPackageNames, customValidatorClassNames);
    logger.info("Built parser configuration '{}' based on configuration in web.xml.", config);
    return config;
  }

  private String checkConfiguration() {
    if (getServletConfig() == null) {
      logger.error("ServletConfig is null.");
      return buildConfigurationErrorMessage(invalidConfigurationMessage);
    } else if (StringUtils.isEmpty(getServletConfig().getInitParameter(MODEL_PACKAGES_CONFIG_PARAM))) {
      logger.error("'{}' parameter missing in web.xml.", MODEL_PACKAGES_CONFIG_PARAM);
      return buildConfigurationErrorMessage(invalidConfigurationMessageIntro, packageConfigurationMissingMessage);
    }
    return StringUtils.EMPTY;
  }

  private String buildConfigurationErrorMessage(String... messages) {
    return Joiner.on("").join(messages);
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

  private List<String> getServletConfigAsList(String paramName) {
    String initParameter = getServletConfig().getInitParameter(paramName);
    if (StringUtils.isEmpty(initParameter)) {
      return Collections.emptyList();
    } else {
      return Arrays.asList(StringUtils.split(initParameter, ','));
    }
  }
}
