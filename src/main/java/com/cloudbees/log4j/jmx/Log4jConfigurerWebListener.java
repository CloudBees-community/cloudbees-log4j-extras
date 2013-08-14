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

import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.management.ManagementFactory;

/**
 * {@link ServletContextListener} to register the {@link Log4jConfigurer} JMX MBean.
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
@WebListener
public class Log4jConfigurerWebListener implements ServletContextListener {

    protected final Logger logger = Logger.getLogger(getClass());
    private MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    private ObjectName objectName;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String path = sce.getServletContext().getContextPath();
        if (path.isEmpty()) {
            path = "/";
        }
        String name = "com.cloudbees:type=Log4jConfigurer,context=" + path + ",name=Log4jConfigurer";
        try {
            objectName = mbeanServer.registerMBean(new Log4jConfigurer(), new ObjectName(name)).getObjectName();
            logger.info("Log4jConfigurer JMX MBean successfully registered with name " + objectName);
        } catch (Exception e) {
            String msg = "Ignore exception registering '" + name + "'";
            sce.getServletContext().log(msg, e);
            logger.warn(msg, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (objectName != null) {
            try {
                mbeanServer.unregisterMBean(objectName);
                logger.info("Log4jConfigurer JMX MBean successfully unregistered of " + objectName);
            } catch (Exception e) {
                String msg = "Ignore exception unregistering '" + objectName + "'";
                sce.getServletContext().log(msg, e);
                logger.warn(msg, e);
            }
        }
    }
}
