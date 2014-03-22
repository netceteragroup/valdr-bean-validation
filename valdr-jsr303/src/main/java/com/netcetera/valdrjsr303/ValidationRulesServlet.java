package com.netcetera.valdrjsr303;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ValidationRulesServlet extends HttpServlet {
  private static final String MODEL_PACKAGES_CONFIG_PARAM = "modelPackages";
  private static final String CUSTOM_VALIDATORS_CONFIG_PARAM = "customValidatorClassNames";
  private ParserConfiguration parserConfiguration;
  private ValidationConfigurationParser parser;

  @Override
  public void init() throws ServletException {
    super.init();
    parserConfiguration = new ParserConfiguration(getServletConfigAsList(MODEL_PACKAGES_CONFIG_PARAM),
      getServletConfigAsList(CUSTOM_VALIDATORS_CONFIG_PARAM));
    parser = new ValidationConfigurationParser(parserConfiguration);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    IOException {
    super.doGet(request, response);
    String json = parser.parse();
    returnJson(response, json);
  }

  private void returnJson(HttpServletResponse response, String json) throws IOException {
    response.getWriter().write(json);
  }

  private List<String> getServletConfigAsList(String paramName) {
    return Arrays.asList(StringUtils.split(getServletConfig().getInitParameter(paramName), ','));
  }
}
