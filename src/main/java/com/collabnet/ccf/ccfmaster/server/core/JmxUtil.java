package com.collabnet.ccf.ccfmaster.server.core;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;

final class JmxUtil {
    public enum JmxStatus {
        CONNECTED, NOT_CONNECTED, NOT_RESPONDING;
    }

    private static final String      JMX_URL_TEMPLATE = "service:jmx:rmi:///jndi/rmi://localhost:%d/jmxrmi";
    private static final Logger      log              = LoggerFactory
                                                              .getLogger(JmxUtil.class);
    private static final TimeLimiter timeLimiter      = new SimpleTimeLimiter();

    private JmxUtil() {
        // prevent instantiation
    }

    /**
     * Connects to JMX on jmxPort and fetches the Memory attribute on
     * "openadaptor:id=SystemUtil"
     * 
     * @param jmxPort
     * @return true if was able to get attribute, false if not successful or if
     *         timeout occurred
     */
    public static JmxStatus canConnect(final int jmxPort) {
        /*
         * IMPORTANT: we need to perform JMX calls in a separate Thread because
         * - according to Johannes - JMX operations have been observed to hang
         * indefinitely.
         */
        return callInSeparateThread(new Callable<JmxStatus>() {

            @Override
            public JmxStatus call() throws Exception {
                String serviceURL = urlForPort(jmxPort);
                JMXConnector connector = null;
                try {
                    JMXServiceURL url = new JMXServiceURL(serviceURL);
                    connector = JMXConnectorFactory.connect(url);
                    MBeanServerConnection connection = connector.getMBeanServerConnection();
                    connection.getAttribute(new ObjectName(
                            "openadaptor:id=SystemUtil"), "Memory");
                    return JmxStatus.CONNECTED;
                } catch (IOException e) {
                    return JmxStatus.NOT_CONNECTED;
                } catch (JMException e) {
                    return JmxStatus.NOT_CONNECTED;
                } finally {
                    try {
                        if (connector != null)
                            connector.close();
                    } catch (IOException e) {
                        return JmxStatus.NOT_CONNECTED;
                    }
                }
            }
        }, JmxStatus.NOT_RESPONDING);
    }

    /**
     * Executes methodName on beanName with params/signatures in a separate
     * thread.
     * 
     * @see MBeanServerConnection#invoke(ObjectName, String, Object[], String[])
     *      for parameter descriptions.
     * @param jmxPort
     * @param beanName
     * @param methodName
     * @param params
     * @param signatures
     * @return the result of the JMX method, null if method timed out.
     */
    public static Object executeMethod(final int jmxPort,
            final String beanName, final String methodName,
            final Object[] params, final String[] signatures) {
        /*
         * IMPORTANT: we need to perform JMX calls in a separate Thread because
         * - according to Johannes - JMX operations have been observed to hang
         * indefinitely.
         */
        return callInSeparateThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                JMXConnector connector = null;
                Object result = null;
                try {
                    String serviceUrl = urlForPort(jmxPort);
                    JMXServiceURL url = new JMXServiceURL(serviceUrl);
                    connector = JMXConnectorFactory.connect(url);
                    MBeanServerConnection connection = connector.getMBeanServerConnection();
                    result = connection.invoke(new ObjectName(beanName),
                            methodName, params, signatures);
                } catch (IOException e) {
                    log.debug("IOException: " + e.getMessage(), e);
                } catch (JMException e) {
                    log.debug("JMX exception: " + e.getMessage(), e);
                } finally {
                    try {
                        if (connector != null)
                            connector.close();
                    } catch (IOException e) {
                        // shouldn't happen.
                        log.error(
                                "error closing JMX connection: "
                                        + e.getMessage(), e);
                    }
                }
                return result;
            }

        }, null);
    }

    public static String urlForPort(int jmxPort) {
        String serviceURL = String.format(JMX_URL_TEMPLATE, jmxPort);
        return serviceURL;
    }

    static <T> T callInSeparateThread(Callable<T> task, T defaultReturnValue) {
        try {
            return timeLimiter.callWithTimeout(task, 3, TimeUnit.SECONDS, true);
        } catch (Exception e1) {
            return defaultReturnValue;
        }
    }

}