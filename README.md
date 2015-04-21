# valdr Bean Validation

[![Build Status](https://travis-ci.org/netceteragroup/valdr-bean-validation.svg?branch=master)](https://travis-ci.org/netceteragroup/valdr-bean-validation)
[![Coverage Status](https://coveralls.io/repos/netceteragroup/valdr-bean-validation/badge.png?branch=master)](https://coveralls.io/r/netceteragroup/valdr-bean-validation?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.valdr/valdr-bean-validation/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.valdr/valdr-bean-validation/)

[Bean Validation](http://beanvalidation.org/) (JSR 303) plugin for [valdr](https://github.com/netceteragroup/valdr),
the new AngularJS Model Validator.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
<!-- **Table of Contents**  *generated with [DocToc](http://doctoc.herokuapp.com/)*-->

  - [Offering](#offering)
  - [Features](#features)
  - [Use](#use)
    - [CLI client](#cli-client)
    - [Servlet](#servlet)
  - [Dependency on valdr](#dependency-on-valdr)
  - [Support](#support)
  - [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->
## Offering

valdr Bean Validation parses Java model classes for Bean Validation constraints (aka JSR 303 annotations)
and extracts their information into a [JSON document to be used by valdr](https://github.com/netceteragroup/valdr#constraints-json). This allows to apply the exact same
validation rules on the server and on the AngularJS client.

## Features

- _offline use:_ CLI client which can be integrated into build process to produce static valdr JSON which is packaged
and delivered with the web application
- _online use:_ Servlet which parses model classes at runtime and sends JSON back to AngularJS client (e.g. during
client start or on-demand)
- both Servlet and CLI client support a number of [config options](https://github.com/netceteragroup/valdr-bean-validation/blob/master/valdr-bean-validation-demo/src/main/resources/valdr-bean-validation.json)
  - list of packages to scan
  - list of classes in those packages to exclude
  - list of fields to exclude
  - list of custom annotation classes to include in JSON
  - whether to output simple or full type names
  - the output file name (CLI only)
  - CORS `Access-Control-Allow-Origin` HTTP header value (Servlet only)
- Servlet offers built-in [CORS](http://en.wikipedia.org/wiki/Cross-origin_resource_sharing) support

## Use

Check out the [demo](valdr-bean-validation-demo) for usage samples of both CLI client and Servlet.

```xml
<dependency>
  <groupId>com.github.valdr</groupId>
  <artifactId>valdr-bean-validation</artifactId>
  <version>see-latest-version-at-the-top-of-this-page</version>
</dependency>
```

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
          <!-- optional, if omitted valdr-bean-validation.json is expected at the root of the class path -->
          <argument>-cf</argument>
          <argument>my-config.json</argument>
          <!-- optional, overrides any 'outputFile' which may have been set in the above config file -->
          <argument>-outputFile</argument>
          <argument>${basedir}/src/main/webapp/validation/validation.json</argument>
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
  <!-- if omitted valdr-bean-validation.json is expected at the root of the class path -->
  <init-param>
    <param-name>configFile</param-name>
    <param-value>my-config.json</param-value>
  </init-param>
</servlet>
```

## Dependency on valdr

valdr Bean Validation is dependent on valdr in two ways:

* [JSON structure](https://github.com/netceteragroup/valdr#constraints-json) is defined by valdr
* validators listed in the JSON document have to be either a [supported valdr valdidator]
(https://github.com/netceteragroup/valdr#built-in-validators) or one of your [custom JavaScript validators](https://github.com/netceteragroup/valdr#adding-custom-validators)

To indicate which valdr version a specific valdr Bean Validation version supports there's a simple rule: the first
digit of the valdr Bean Validation version denotes the supported valdr version. Version 1.x will support valdr 1.
This means that valdr Bean Validation 1.x+1 may introduce breaking changes over 1.x because the second version digit
kind-of represents the "major" version.

## Support

Open a question on SO and tag it with [`valdr-bean-validation`](http://stackoverflow.com/tags/valdr-bean-validation).

## License

[MIT](http://opensource.org/licenses/MIT) © Netcetera AG
