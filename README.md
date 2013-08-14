# CloudBees Log4J Extras

## Log4jConfigurer JMX MBean

The Log4jConfigurer JMX MBean exposes main Log4j configuration information and operations via JMX

### Log4jConfigurer attributes and operations

* ObjectName: `com.cloudbees:type=Log4jConfigurer,context=/myapp,name=Log4jConfigurer` where `context` is the application context (e.g. "/" for the root context or "/myapp" for an app with context "myapp")
* Attributes
  * `String[] LoggerList`: list of active loggers
  * `String Log4jConfiguration`: get Log4j effective configuration rendered in the properties format
* Operations
 * `String getLoggerLevel(loggerName)`: logger level or `null` if logger is not defined or if the level of this logger is not defined.
 * `String getLoggerEffectiveLevel(loggerName)`: effective logger level, never `null`
 * `void setLoggerLevel(loggerName, level)`: set the level of the given logger. If the given level is `null or unknown level will set logger level to `null`
 * `void reloadDefaultConfiguration()`: reload Log4j default configuration

### Embedded Log4jConfigurer JMX MBean in a Servlet 3+ application

Just add cloudbees-log4j-extras to your classpath, and let your Servlet 3+ container discover the
`com.cloudbees.log4j.jmx.Log4jConfigurerWebListener` class that is annotated with `@WebListener`.

Please note that `@WebListener` annotated classes are discovered if you enable classpath scanning with `metadata-complete="false"`
in web.xml.

```xml
<web-app
        xmlns="http://java.sun.com/xml/ns/javaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
        version="3.0" metadata-complete="false">
   ...
</web-app>
```

### Embedded Log4jConfigurer JMX MBean in a Servlet 2 application

Declare a `com.cloudbees.log4j.jmx.Log4jConfigurer` listener in web.xml

```xml
<web-app ...>
    <listener>
        <listener-class>com.cloudbees.log4j.jmx.Log4jConfigurer</listener-class>
    </listener>
</web-app>
```