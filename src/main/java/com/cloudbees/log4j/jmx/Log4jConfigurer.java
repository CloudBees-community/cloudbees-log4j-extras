/*
 * Copyright 2010-2013, the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudbees.log4j.jmx;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.config.PropertyPrinter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * JMX Mbean wrapper of the Log4j {@link LogManager}.
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class Log4jConfigurer implements Log4jConfigurerMBean {

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * Returns the list of active loggers.
     *
     * @return logger names
     */
    @Nonnull
    public String[] getLoggerList() {
        try {
            Enumeration<Logger> currentLoggers = LogManager.getLoggerRepository().getCurrentLoggers();
            List<String> loggerNames = new ArrayList<String>();
            while (currentLoggers.hasMoreElements()) {
                loggerNames.add(currentLoggers.nextElement().getName());
            }
            return loggerNames.toArray(new String[0]);
        } catch (RuntimeException e) {
            logger.warn("Exception getting logger names", e);
            throw e;
        }
    }

    /**
     * Get the declared level of the given logger.
     *
     * @param loggerName logger name
     * @return logger level or <code>null</code> if logger is not defined or if the level of this logger is not defined.
     */
    @Nullable
    public String getLoggerLevel(@Nullable String loggerName) {
        try {
            loggerName = loggerName == null ? "" : loggerName;
            if (loggerName.isEmpty()) {
                return LogManager.getRootLogger().getLevel().toString();
            }

            Logger logger = LogManager.exists(loggerName);
            if (logger == null) {
                return null;
            } else {
                Level level = logger.getLevel();
                if (level == null) {
                    return null;
                }
                return level.toString();
            }
        } catch (RuntimeException e) {
            logger.warn("Exception getting effective logger level " + loggerName, e);
            throw e;
        }
    }

    /**
     * Get the logger level that applies to the given logger name.
     *
     * @param loggerName logger name
     * @return effective logger level, never <code>null</code>
     */
    @Nonnull
    public String getLoggerEffectiveLevel(@Nullable String loggerName) {
        try {
            loggerName = loggerName == null ? "" : loggerName;

            if (loggerName.isEmpty()) {
                return LogManager.getRootLogger().getLevel().toString();
            }

            while (true) {
                Logger logger = LogManager.exists(loggerName);
                if (logger == null || logger.getLevel() == null) {
                    int idx = loggerName.lastIndexOf('.');
                    if (idx > 0) {
                        loggerName = loggerName.substring(0, idx - 1);
                    } else {
                        return LogManager.getRootLogger().getLevel().toString();
                    }
                } else {
                    return logger.getLevel().toString();
                }
            }
        } catch (RuntimeException e) {
            logger.warn("Exception getting effective logger level " + loggerName, e);
            throw e;
        }
    }

    /**
     * Set the level of the given logger.
     *
     * @param loggerName name of the logger to set
     * @param level      new level. <code>null</code> or unknown level will set logger level to <code>null</code>
     * @see Logger#setLevel(org.apache.log4j.Level)
     */
    public void setLoggerLevel(@Nonnull String loggerName, @Nullable String level) {
        logger.info("setLoggerLevel(" + loggerName + "," + level + ")");
        try {
            Level levelAsObject = Level.toLevel(level);
            LogManager.getLogger(loggerName).setLevel(levelAsObject);
        } catch (RuntimeException e) {
            logger.warn("Exception setting logger " + loggerName + " to level " + level, e);
            throw e;
        }
    }

    /**
     * @return log4j configuration in a properties file format
     * @see PropertyPrinter
     */
    @Nonnull
    public String getLog4jConfiguration() {
        try {
            StringWriter writer = new StringWriter();
            PropertyPrinter propertyPrinter = new PropertyPrinter(new PrintWriter(writer));
            propertyPrinter.print(new PrintWriter(writer));
            return writer.toString();
        } catch (RuntimeException e) {
            logger.warn("Exception generating log4j configuration", e);
            throw e;
        }
    }

    /**
     * @see LogManager#resetConfiguration()
     */
    public void reloadDefaultConfiguration() {
        logger.info("reloadDefaultConfiguration()");
        try {
            LogManager.resetConfiguration();
        } catch (RuntimeException e) {
            logger.warn("Exception resetting log4j configuration", e);
            throw e;
        }
    }
}
