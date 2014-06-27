# Changelog

## 1.1.0 - unreleased

###Bugfixes###

###Features###

- [#16](https://github.com/netceteragroup/valdr-bean-validation/issues/16), allow configuration via JSON file,
**breaking change**
- [#17](https://github.com/netceteragroup/valdr-bean-validation/issues/17), allow exclusion of classes
- [#18](https://github.com/netceteragroup/valdr-bean-validation/issues/18), allow exclusion of fields

###Upgrade notes###
The individual config parameters for the CLI client and the Servlet were removed in favor of the new [JSON config
file](https://github.com/netceteragroup/valdr-bean-validation/blob/master/valdr-bean-validation-demo/src/main
/resources/valdr-bean-validation.json). Besides being a lot more concise and easier to handle this gives you the
comfort of using the exact same config file for both CLI client and the Servlet.

- The CLI client now accepts a `-cf` argument to define the path to JSON configuration file. If omitted
`valdr-bean-validation.json` is expected at root of class path.
- The Servlet can be configured the same way using a `configFile` init parameter.

## 1.0.0 - 2014-06-04
- first official release