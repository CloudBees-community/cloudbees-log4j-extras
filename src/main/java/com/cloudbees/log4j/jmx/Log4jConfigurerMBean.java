/*
 * Copyright 2010-2013, CloudBees Inc.
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

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public interface Log4jConfigurerMBean {
    String[] getLoggerList();

    /**
     * @param loggerName
     * @return logger level or <code>null</code> if logger is not defined
     */
    String getLoggerLevel(String loggerName);

    /**
     * @param loggerName
     * @return effective logger level, never <code>null</code>
     */
    String getLoggerEffectiveLevel(String loggerName);

    void setLoggerLevel(String loggerName, String level);

    /**
     * @return log4j configuration in a properties file format
     */
    String printLog4jEffectiveConfiguration();

}
