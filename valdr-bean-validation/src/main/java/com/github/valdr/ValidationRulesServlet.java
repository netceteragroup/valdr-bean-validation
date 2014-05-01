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

/**
 * Produces JSON validation rules on the fly parsing model classes in your classpath. Servlet can/must be configured
 * using three {@link javax.servlet.ServletConfig} parameters (* = mandatory):
 * <ul>
 * <li>modelPackages*: comma-separated list of package names such as "com.company.foo,com.company.bar"</li>
 * <li>customAnnotationClassNames: comma-separated list of custom validator annotation class names,
 * by default only the ones from {@link BuiltInConstraint} are processed</li>
 * <li>corsAllowOriginPattern: in case this Servlet is deployed under a different domain than the valdr client
 * you can use <a href="http://en.wikipedia.org/wiki/Cross-origin_resource_sharing">CORS</a> to access this
 * resource</li>
 * </ul>
 * <p/>
 * Upon each request the configured model packages in the classpath are parsed for classes containing supported or
 * custom Bean Validation annotations. The Servlet then builds and returns a JSON document with all validation rules
 * (i.e. Bean Validation constraints). The JSON document adheres to structure specified by valdr.
 */
public class ValidationRulesServlet extends HttpServlet {
  private static final String MODEL_PACKAGES_CONFIG_PARAM = "modelPackages";
  private static final String CUSTOM_ANNOTATIONS_CONFIG_PARAM = "customAnnotationClassNames";
  private static final String CORS_PATTERN_CONFIG_PARAM = "corsAllowOriginPattern";
  private final Logger logger = LoggerFactory.getLogger(ValidationRulesServlet.class);
  private boolean correctlyConfigured = false;
  private String corsAllowOriginPattern;
  private String invalidConfigurationMessage;
  private ConstraintParser parser;

  @Override
  public void init() throws ServletException {
    super.init();
    invalidConfigurationMessage = checkConfiguration();
    if (StringUtils.isEmpty(invalidConfigurationMessage)) {
      correctlyConfigured = true;
      logger.info("The Servlet appears to be correctly configured.");
      corsAllowOriginPattern = getCorsAllowOriginPattern();
      parser = new ConstraintParser(buildParserConfiguration());
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
    List<String> customAnnotationClassNames = getServletConfigAsList(CUSTOM_ANNOTATIONS_CONFIG_PARAM);
    ParserConfiguration config = new ParserConfiguration(modelPackageNames, customAnnotationClassNames);
    logger.info("Built parser configuration '{}' based on configuration in web.xml.", config);
    return config;
  }

  private String checkConfiguration() {
    if (getServletConfig() == null) {
      return handleServletContextNull();
    } else if (StringUtils.isEmpty(getServletConfig().getInitParameter(MODEL_PACKAGES_CONFIG_PARAM))) {
      return handleModelPackagesNotConfigured();
    }
    return StringUtils.EMPTY;
  }

  private String handleServletContextNull() {
    logger.error("ServletConfig is null.");
    return buildConfigurationErrorMessage(invalidConfigurationMessage);
  }

  private String handleModelPackagesNotConfigured() {
    logger.error("'{}' parameter missing in web.xml.", MODEL_PACKAGES_CONFIG_PARAM);
    return buildConfigurationErrorMessage("The Servlet is not configured correctly. ",
      "The '" + MODEL_PACKAGES_CONFIG_PARAM + "' parameter in web.xml is missing.");
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
