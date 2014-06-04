# valdr Bean Validation [![Build Status](https://travis-ci.org/netceteragroup/valdr-bean-validation.svg?branch=master)](https://travis-ci.org/netceteragroup/valdr-bean-validation) [![Coverage Status](https://coveralls.io/repos/netceteragroup/valdr-bean-validation/badge.png?branch=master)](https://coveralls.io/r/netceteragroup/valdr-bean-validation?branch=master)

[Bean Validation](http://beanvalidation.org/) (JSR 303) plugin for [valdr](https://github.com/netceteragroup/valdr),
the new AngularJS Model Validator.

## Offering

valdr Bean Validation parses Java model classes for Bean Validation constraints (aka JSR 303 annotations)
and extracts their information into a JSON document to be used by valdr. This allows to apply the exact same
validation rules on server and on the AngularJS client.

## Features

- _offline use:_ CLI client which can be integrated into build process to produce static valdr JSON which is packaged
and delivered with the web application
- _online use:_ Servlet which parses model classes at runtime and sends JSON back to AngularJS client (e.g. during
client start or on-demand)
- both Servlet and CLI client support the same config options
  - list of packages to scan
  - list of custom annotation classes to include in JSON
  - more to come: [#16](/../../issues/16), [#17](/../../issues/17), [#18](/../../issues/18), [#19](/../../issues/19)
- Servlet offers built-in [CORS](http://en.wikipedia.org/wiki/Cross-origin_resource_sharing) support

## Use

Check out the [demo](valdr-bean-validation-demo) for usage samples of both CLI client and Servlet.

### CLI client

Example of Maven integration:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>${exec-maven-plugin.version}</version>
      <executions>
        <execution>
          <id>process-bean-validation-annotations</id>
          <phase>process-classes</phase>
          <goals>
            <goal>java</goal>
          </goals>
        </execution>
      </executions>
      <configuration>
        <mainClass>com.github.valdr.cli.ValdrBeanValidation</mainClass>
        <arguments>
          <argument>-p</argument>
          <argument>com.github.valdr.demo.model</argument>
          <argument>-c</argument>
          <argument>org.hibernate.validator.constraints.CreditCardNumber</argument>
          <argument>-o</argument>
          <!--rules.json is written to target/valdr-bean-validation-demo-<version>
              i.e. it'll be in the root folder of the final WAR file -->
          <argument>${project.build.directory}/${project.build.finalName}/rules.json</argument>
        </arguments>
      </configuration>
    </plugin>
  </plugins>
</build>
```

### Servlet

Example of web.xml:
```xml
<servlet>
  <servlet-name>valdr Bean Validation Servlet</servlet-name>
  <servlet-class>com.github.valdr.ValidationRulesServlet</servlet-class>
  <init-param>
    <param-name>modelPackages</param-name>
    <param-value>com.github.valdr.demo.model</param-value>
  </init-param>
  <init-param>
    <param-name>customAnnotationClassNames</param-name>
    <param-value>org.hibernate.validator.constraints.CreditCardNumber</param-value>
  </init-param>
  <init-param>
    <param-name>corsAllowOriginPattern</param-name>
    <param-value>*</param-value>
  </init-param>
</servlet>
```

## License

[MIT](http://opensource.org/licenses/MIT) © Netcetera AG
